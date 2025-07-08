from waitress import serve
from flask import Flask, request, jsonify
from app.service.messageService import MessageService
from kafka import KafkaProducer
import json
import os

app = Flask(__name__)
app.config.from_pyfile('config.py', silent=True)

messageService = MessageService()
kafka_host = os.getenv('KAFKA_HOST', 'kafka')
kafka_port = os.getenv('KAFKA_PORT', '9092')
kafka_bootstrap_servers = f"{kafka_host}:{kafka_port}"
print(f"Kafka server is {kafka_bootstrap_servers}\n")

producer = KafkaProducer(
    bootstrap_servers=kafka_bootstrap_servers,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

@app.route('/v1/ds/message', methods=['POST'])
def handle_message():
    user_id = request.headers.get('x-user-id')
    if not user_id:
        return jsonify({'error': 'x-user-id header is required'}), 400
    message = request.json.get('message')
    if not message:
        return jsonify({'error': 'Message body is required'}), 400

    result = messageService.process_message(message)
    if result:
        result['user_id'] = user_id
        producer.send('expense_service', result)
        return jsonify(result), 200
    return jsonify({'error': 'Invalid message format'}), 400

@app.route('/', methods=['GET'])
def home():
    return 'Hello world'

@app.route('/health', methods=['GET'])
def health_check():
    return 'OK'

if __name__ == "__main__":
    serve(app, host="localhost", port=8010)

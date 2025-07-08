FROM --platform=linux/amd64 python:3.13.5

WORKDIR /app

# Copy the full application including src/
COPY dist/ds_service-1.0.0.tar.gz .
# Copy the .env file
COPY src/app/.env .



# Install the tar.gz package
RUN pip install --no-cache-dir ds_service-1.0.0.tar.gz

# Set environment variables
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=8010

# Set the Python path so 'src' can be found
ENV PYTHONPATH=/app

EXPOSE 8010

CMD ["flask", "run"]

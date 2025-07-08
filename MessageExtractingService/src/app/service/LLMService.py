import os
from dotenv import load_dotenv
from langchain_mistralai import ChatMistralAI
from langchain_core.prompts import ChatPromptTemplate
from pydantic import BaseModel, Field
from langchain_core.utils.function_calling import convert_to_openai_tool

# Load environment variables
load_dotenv()

# Define the structured output model (you can also import this if defined elsewhere)
class Expense(BaseModel):
    amount: str | None = Field(description="Transaction amount")
    currency: str | None = Field(description="Currency used, like Rs, INR, etc.")
    merchant: str | None = Field(description="Merchant or vendor where transaction happened")

class LLMService:
    def __init__(self):
        api_key = os.getenv("MISTRAL_API_KEY")
        if not api_key:
            raise ValueError("MISTRAL_API_KEY not found in .env")

        self.prompt = ChatPromptTemplate.from_messages([
            ("system", 
             "You are a financial assistant. Extract only the structured data from the message. "
             "If a value is missing or unclear, set it to null."),
            ("human", "{text}")
        ])

        self.llm = ChatMistralAI(
            api_key=api_key,
            model="mistral-large-latest",  # or mistral-small or mistral-tiny
            temperature=0
        )

        # Create a pipeline: prompt -> LLM -> structured output
        self.runnable = self.prompt | self.llm.with_structured_output(schema=Expense)

    def runLLM(self, message: str) -> dict | None:
        try:
            result = self.runnable.invoke({"text": message})
            return result.dict()
        except Exception as e:
            print("Mistral LLM Error:", e)
            return None

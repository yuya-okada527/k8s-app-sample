FROM tiangolo/uvicorn-gunicorn-fastapi:python3.7

COPY ./app /app
ADD requirements.txt /app

RUN pip install -r requirements.txt

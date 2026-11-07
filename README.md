# spring-ai

## swagger

http://localhost:8080

## pgadmin

http://localhost:5050

pgadmin4@pgadmin.org
admin

## docker

```commandline
docker compose up -d

docker compose down
```

## curl

```commandline

curl http://localhost:11431/api/generate \
-H "Content-Type: application/json" \
-d '{
    "model": "gemma3:4b-it-q4_K_M",
    "prompt": "Bohemian Rhapsody full text",
    "stream": true,
    "options": {
        "num_predict": 100,
        "temperature": 0.7,
        "top_k": 40,
        "top_p": 0.9,
        "repeat_penalty": 1.1
    }
}'

```

Параметры:

- **temperature** - параметр, который помогает подбирать вероятность того, насколько токен подходит по смыслу. Чем выше
  температура, тем больше бреда - токенов, которые с малой вероятностью. Чем ниже температура, тем однообразнее и
  скучнее ответы.
- **top_k** - параметр, который говорит из скольки токенов выбирать максимум, - первые k наиболее частых вариантов
  ответов в зависимости от контекста.
- **top_p** - параметр, который отвечает за качество параметров, т. е. первые p процентов вероятности появления top_k
  вариантов.
- **repeat_penalty** - параметр, который штрафует модель за повторяемость вариантов

## Принципы:

- Знать не многое, но нужное.

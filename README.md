# quotes-service

Сервис для сохранения котировок и получения elvl по инструментам (бумагам) по REST API.

Swagger is available at 
```http request
https://quotes-test-service.herokuapp.com/quotes/swagger-ui/
```

####При первом запросе нужно минутку подождать пока запуститься приложение.

### Requests

Create new quote
```http request
curl --request POST \
  --url https://quotes-test-service.herokuapp.com/quotes/new \
  --header 'Content-Type: application/json' \
  --data '{
	"isin": "RU000A0JX0J1",
	"ask": 101.6,
	"bid": 100.9
}'
```
Get quote
```http request
curl --request GET \
  --url 'https://quotes-test-service.herokuapp.com/quotes/quote?isin=RU000A0JX0J1'
```

Get all quotes
```http request
curl --request GET \
  --url 'https://quotes-test-service.herokuapp.com/quotes/quotes?isin=RU000A0JX0J1'
```
В проекте три сервиса

* HelloApplication http://localhost:8080/swagger-ui.html 
* RandomApplication http://localhost:8090/swagger-ui.html
* SleepingApplication http://localhost:8085/swagger-ui.html

HelloApplication вызывает RandomApplication, который генерит случайное число, по которому определяется приветствие.
Если RandomApplication возвращает ошибку или не запущен, HelloApplication возвращает дефолтное приветствие.
SleepingApplication обращается к RandomApplication для определения времени, 
на которое уснуть после того как был вызван метод контроллера с параметром максимального времени для сна.
Настроен RandomProxyFallbackFactory для логирования ошибки и дефолтного времени сна.
Также настроен Hystrix для ограничения максимального времени работы метода.
HelloService также использует Hystrix для вызова дефолтного метода.


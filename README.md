#Описание
Калькулятор на javafx, с сохранением истории последних 10 операций

#Требования
* Java 11+
* maven 3.3.9+

#Калькулятор
Поддерживает 9 знаков после запятой с математическим округлением последней цифры.

Умеет добавлять нуль, если точка нажата без предшествующей цифры.

При нажатии на кнопку history выводит 10 последних операций.

При делении на нуль выводит "Division by zero!", операция считается ошибочной и не записывается в базу.
 
#Тестирование
Есть 2 режима для запуска тестов:
* headless - без визуализации (по умолчанию).
* headful - с визуализацией пользовательских действий
 
Для выбора режима необходимо установить флаг мавена headless (true для headless, false для headful) задав переменную в pom-файле либо передав в строку запуска

Пример скрипта сборки
```
mvn clean package -Dheadless=false
```

Тестовая база данных создаётся во временном файле, который автоматически удаляется после окончания тестов.

#Запуск

Запуск проекта можно осуществить из ide
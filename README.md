# Shopping-Microservice
Spring Boot project written in the microservice architecture.
## The overall flow of the project.
The user chooses the products he wants and sends a request to the order service. The order service sends a request to the product service and gets all the information of the product. Then, a request is sent to the inventory service via the **web client** for stock control. If all products are in stock, the order is processed successfully. Business logic is being processed. Finally, the order's information is sent to the notification service asynchronously via **kafka**. And information about the order is sent to the user's e-mail.<br>
### The technologies used in the project
1) Spring Boot
2) MongoDB
3) MySQL
4) Apache Kafka

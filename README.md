
# Home Assignment for Citadele

Simple financing application aggregator service


## Author

- [@Aleksandrs Musaluks](https://github.com/Vestamix)


## Installation

1) Checkout project
2) Navigate to directory
3) Build project with gradle
```bash
  ./gradlew build
```
4) Run jar

## Workflow

1. Client fills the form

![Filling form](https://i.postimg.cc/N0tDG391/filliing-Form.png)

2. The form is hidden, client is asked to wait for response

![Waiting for response](https://i.postimg.cc/XNxYsdk3/Screenshot-2023-09-01-at-09-21-43.png)

During that service does validation of provided information

If something's wrong client will receive error message and the form will be available for editing:

![Error](https://i.postimg.cc/tTyJvHSm/Screenshot-2023-09-01-at-09-48-49.png)

Then service sends clients information to 2 external services, getting unique IDs from each, which then
are stored in database in same table. That id is returned as response.

Next request checks status of application using that provided id.


**UI is repeating requests every 5 seconds, until receives status PROCESSED**

The status depends on external services response:
- Both are draft - returns DRAFT
- Both failed - returns PROCESSED
- One is draft, another is processed - returns DRAFT
- One is not responding, another is processed - returns PROCESSED
- Both are processed - returns PROCESSED

When status is PROCESSED, means that UI can check what is offered for client as a list.

3. Client receives offers

![Offers](https://i.postimg.cc/ncm2B4mV/Screenshot-2023-09-01-at-09-41-09.png)

Clicking on button:

![Confirmation](https://i.postimg.cc/JnqNPxqZ/Screenshot-2023-09-01-at-09-53-54.png)


If there are no offers:

![NoOffers](https://i.postimg.cc/2SnN3LGg/Screenshot-2023-09-01-at-09-59-00.png)

## Workflow diagram

![Request](https://i.postimg.cc/wTwvWLHL/diag1.png)

## Tech Stack

**Client:** HTML, JavaScript

**Server:** Java 17, Spring-boot, Lombok, jUnit


# bs-australian-super-demo

## Set following env variables before running tests

`` export BROWSERSTACK_USERNAME=<browserstack-username>
``

`` export BROWSERSTACK_ACCESS_KEY=<browserstack-accesskey>
``

`` export email_id=<email_id>
``

`` export passowrd=<password>
``

`` export rec_email_id=<rec_email_id>
``

## Running tests

* Run single test : `mvn test -P single`
* Run parallel test : `mvn test -P parallel`
* Run local test : `mvn test -P local`
* Run parallel suite : `mvn test -P parallel_suite`
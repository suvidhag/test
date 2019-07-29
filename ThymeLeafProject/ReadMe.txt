To execute the program Run as Java Application, Embedded Tomcat server will run.

Go to browser -> http://localhost:8080/dateForm
- Enter Start Date and EndDate fields and click on the CalculateDiff btton to see the result.

To configure the error message, set te following configurations in application.properties :

error.message=Start Date should be before End Date!
error.startDateMessage=Start Date should be specified
error.endDateMessage=End Date should be specified
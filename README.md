# About this project
This was a project that was given to me during my summer 2022 term at Portland State University in the "Advanced Programming with Java" class where we did incremental development on various projects that culminated our knowledge to this point. All skills and tools we learned throughout the term were put to the test for this project and required us to develop a simple (but functional) toy android application to "manage" customer phone bills.

The previous projects took on different forms using basic command line programs which took in command line arguments to handle the user’s request. Each of these projects required tests to support sufficient code coverage and included documentation as well.

- The first required simply adding and tracking a single customers bill
- The second required tracking the customer’s bill in a text file
- The third required tracking multiple customers, the dates, times, duration, and sorting based on the date/time. If date/time were the same, then sort by the phone number and finally "pretty printing" all this data to make it human readable.
- The fourth used the REST API and utilized a Java servlet to handle requests from the command line. The user would enter in data to request/add/search for and the servlet would respond. All data was kept in volatile memory and not stored permanently in any sort of way.

The requirements were fairly similar to the previous projects (aside from the fourth). It had to store data, be human readable, and searchable.



# How was this made? What tools/languages were used? What did you learn?
This project was developed using Android Studio and written using Java and XML. Java for the executable code, XML for the design and layout of the app. The template used within Android Studio was "Empty Activity" and the device being emulated was a Pixel 2 using Android API 29.

This project didn't require documentation or testing. The Professor felt it was more than enough given both the time constraints and our demonstration in previous projects was sufficient to allow us to just have fun and learn for this last project.



# How to run this project
Download and install Android Studio. Open the folder as the project in AS, then build it and install on the **small** handheld device of your choice.

 ***OR***

Use the emulated device provided to explore within Android Studio as well under "Device Manager" and running it through there.


# How long did this take/How much time did you have?
We were provided 2 weeks to complete the project, in which I spent about 30 hours creating it from the ground up and utilizing some code from the previous projects.


# What challenges did you face in this project?
Getting used to a new IDE and general development environment/process. This wasn't terribly different from what I had experienced so far, but just different enough there was a learning curve to be had between understanding how Android layouts work, XML, and just connecting the dots between the app and the Java code itself.

There was also just learning the Java Android libraries as well. The documentation is thorough and has great information. But the presentation and structure made it particularly difficult to narrow down exactly which piece I was looking for and putting it together with other parts. One point of focus was how to get alerts to pop up the right way and requesting user input as well as establishing click listeners to work as expected.

# Budget Tracker Application

## Description
The Budget Tracker Application is a Java application designed to help individuals manage their personal finances. It provides a user-friendly interface for tracking income, expenses, and budget goal. Users can categorize their transactions, view monthly summaries and use that to make informed financial decisions.

## Target Users
The target users for this application could be:
- **College Students:** Students usually have limited financial resources and can benefit from a budget tracker to manage tuition, rent, textbooks and other miscellaneous expenses.
- **Families:** Families have multiple income sources and expenses, including groceries, utilities and childcare. They can use the application to view an overview of their finances.
- **Individual:** Individuals who want to monitor their daily expenses and savings.

## Interesting?
This project interests me for the following reasons:
- **Skill Development:** Building a budget tracker application will provide an opportunity to develop my programming skills, particularly in Java.
- **Practical Use:** It has practical purpose of helping users manage their finances. Developing a budget tracker will help me understand my own financial habits better.

## User Stories
- As a user, I want to be able to add sources of income and expenses (along with categories).
- As a user, I want to view a history of all my income and expense transactions, including their categories and dates.
- As a user, I want to manage my expense categories by adding new categories.
- As a user, I want to view a pie chart of my monthly expenses on the basis of categories.
- As a user, I want to save my data.
- As a user, I want to load my data.

## Instructions for Grader
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by expense tab entering data and clicking on "Add Expense" button
- You can generate the second required action related to the user story "adding multiple Xs to a Y" by income tab, entering data and clicking on "Add Income" button
- Can view the expense and income in the MonthlyReport tab, by selecting month/year and clicking on Generate Monthly Report
- Visual component, generate Expense Distribution by Category pie chart for a given month and year
- For loading data, there is a pop-up at the starting of the application to load data
- For saving data, there is a pop-up while closing the application, asking if user wants to save data.

## Phase 4: Task 2
Log:
- Fri Dec 01 16:38:41 PST 2023
- Expense: 123.0 for December 2023 added

- Fri Dec 01 16:38:48 PST 2023
- Income: 344.0 for December 2023 added

- Fri Dec 01 16:38:56 PST 2023
- Added new category: School

- Fri Dec 01 16:39:06 PST 2023
- Budget for December 2023 is set to: 5000.0

- Fri Dec 01 16:39:09 PST 2023
- Monthly report for January 2023 generated

- Fri Dec 01 16:39:15 PST 2023
- Monthly report for December 2023 generated

- Fri Dec 01 16:39:19 PST 2023
- Pie chart for December 2023 generated

- Fri Dec 01 16:39:23 PST 2023
- Data saved

## Phase 4: Task 3
- To improve my design, I could have designed the monthMap (which is repeating in many of my classes) to another class and called to class whenever needed
- I could also refactor TransactionExpense and TransactionIncome to implement a class as they mostly do the same thing, just to different things.
- Same could be done for the Income and Expense Classes
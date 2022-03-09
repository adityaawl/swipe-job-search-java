# Welcome to Swipejobs search engine

It's a Job search engine as the name suggest. It communicates to the CORE system and loads all the 
available Jobs and perform a match best on the Worker's profile & preferences.

## Requirements
This is Maven project and require Java 11.

### Run the application

Run the application which will be listening on port `7777`.
```console
$ ./mvn spring-boot:run
```

## Job Search API

Endpoint

```text
GET /jobs/recommend/<workerId>[?limit=<limit>]
```

Parameters

| Parameter      | Description                                                  |
| -------------- | -------------------------------------------------------------|
| `workerId`     | Worker userId for which job search is performed.             |
| `limit`        | Optional. Maximum number of Jobs in result. Default is 3.    |


CURL command:

```console
$ curl --location --request GET 'http://localhost:7777/jobs/recommend/46'
```

Example Output:
```json
[
    {
        "jobId": 32,
        "guid": "562f66aa212614a7e0c9dee1",
        "company": "Solaren",
        "jobTitle": "Creator of opportunities",
        "about": "In eu cupidatat commodo Lorem Lorem laboris ipsum anim incididunt nisi. Amet nostrud exercitation tempor exercitation reprehenderit labore anim officia do consequat fugiat amet. Ipsum deserunt incididunt exercitation velit Lorem non velit cupidatat ea anim. Eiusmod aliquip esse ea ullamco voluptate consequat velit ipsum. Mollit non aliqua consectetur ut est adipisicing aliqua.",
        "startTime": null,
        "workersRequired": 2,
        "billRate": "$5.44",
        "location": {
            "longitude": "14.014828",
            "latitude": "50.119086"
        },
        "requiredCertificates": [
            "The Asker of Good Questions",
            "Outstanding Innovator",
            "The Behind the Scenes Wonder"
        ],
        "driverLicenseRequired": false
    },
    {
        "jobId": 26,
        "guid": "562f66aad42092ef776f7ccb",
        "company": "Zytrac",
        "jobTitle": "Ambassador of buzz",
        "about": "Eiusmod velit ad et aliquip sint incididunt non excepteur ut consequat ullamco occaecat. Excepteur ullamco tempor ut est. Labore do voluptate dolore elit. Ea dolor voluptate cupidatat cupidatat non ad cillum pariatur in. Id aliqua laborum ut voluptate laboris elit. Commodo mollit proident proident voluptate. Tempor consectetur minim reprehenderit aute ea quis tempor minim adipisicing proident exercitation magna tempor.",
        "startTime": null,
        "workersRequired": 2,
        "billRate": "$19.79",
        "location": {
            "longitude": "14.013835",
            "latitude": "49.994037"
        },
        "requiredCertificates": [
            "The Asker of Good Questions"
        ],
        "driverLicenseRequired": false
    },
    {
        "jobId": 29,
        "guid": "562f66aa7f96c1f61adfd108",
        "company": "Centice",
        "jobTitle": "The Resinator",
        "about": "Non veniam ipsum esse consectetur cillum ipsum quis aliquip. In eiusmod excepteur laborum laboris quis cupidatat consectetur exercitation veniam nisi. Commodo officia cillum laboris velit pariatur mollit deserunt mollit. Commodo sit proident aliquip officia. Irure deserunt ullamco quis aliqua eu eu ullamco qui eu minim. Tempor pariatur est ullamco duis reprehenderit. Est qui non Lorem et.",
        "startTime": null,
        "workersRequired": 2,
        "billRate": "$14.98",
        "location": {
            "longitude": "14.082219",
            "latitude": "50.180255"
        },
        "requiredCertificates": [
            "Outstanding Memory Award"
        ],
        "driverLicenseRequired": false
    }
]
```
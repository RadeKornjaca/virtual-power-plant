#/bin/bash

# Add multiple batteries
curl --location --request POST 'localhost:8080/batteries' \
--header 'Content-Type: application/json' \
--data-raw '[
    {
        "name" : "Test3",
        "postcode" : 22000,
        "wattCapacity" : 4000
    },
    {
        "name" : "Test4",
        "postcode" : 23000,
        "wattCapacity" : 5000
    },
    {
        "name" : "Test1",
        "postcode" : 11000,
        "wattCapacity" : 10000
    },
    {
        "name" : "Test2",
        "postcode" : 21000,
        "wattCapacity" : 7500
    }
]'

# Filter batteries by postcode
curl --location --request GET 'http://localhost:8080/batteries?fromPostcode=21000&toPostcode=23000'


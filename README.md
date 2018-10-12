## Shipping - ZipCode

A project where a shipment of a product is delivery is determined by consolidating a list 
of zip code ranges.

##### BACKGROUND
---
Sometimes items cannot be shipped to certain zip codes, and the rules for these restrictions are stored as a series of ranges of 5 digit codes. 
For example if the ranges are:

[94133,94133] [94200,94299] [94600,94699]

Then the item can be shipped to zip code 94199, 94300, and 65532, but cannot be shipped to 94133, 94650, 94230, 94600, or 94299.

Any item might be restricted based on multiple sets of these ranges obtained from multiple sources.

###### PROBLEM
---
Given a collection of 5-digit ZIP code ranges (each range includes both their upper and lower bounds), 
provide an algorithm that produces the minimum number of ranges required to represent the same restrictions as the input.

###### NOTES
- The ranges above are just examples, your implementation should work for any set of arbitrary ranges
- Ranges may be provided in arbitrary order
- Ranges may or may not overlap
- Your solution will be evaluated on the correctness and the approach taken, and adherence to coding standards and best practices.

###### EXAMPLES:
If the input = [94133,94133] [94200,94299] [94600,94699]
Then the output should be = [94133,94133] [94200,94299] [94600,94699]

If the input = [94133,94133] [94200,94299] [94226,94399] 
Then the output should be = [94133,94133] [94200,94399]


#### Prerequisites

1. Java
2. Maven

#### Run
To run the application
```
mvn exec:java
```


##### Run Unit tests

To run all the unit tests.
```
mvn test
```

#### Others
Location of input file (dataSource):  ```src/main/resources/ListOfShippingZipCodeRanges.json```







# Converter of computer data exchange formats
# JSON, XML, YAML and CSV
> Project as part of university subject "Teoria kompilacji i kompilatory"

> Authors: Agata Korzeniowska, Adam Surówka, Maciej Syska,  Mateusz Kastura
---
### Changelog
| Milestone | Date  | Info |
| :------------ |:---------------:| -----:|
|  #1 | 20.03.2020 | Document creation, initial specification|
|  #2 | 12.06.2020 |   Documentation improvements |
|  #3 | 16.06.2020 |   App diagram and testing solution |

---
# Overall Decription
Project goal is to develop easy to use both ways converter for data exchange formats such as JSON, XML, YAML i CSV. Input file, output format and output file path are chosen by user. 

---
# Functionalities
Program is to follow algorithm below:
                
1. Getting inuput file path from the user
2. Getting output file path from the user
3. Getting output file format from the user
4. Validation of given arguments
5. Verification of input file extension, reading the file and validation of input file structure
6. Converstion into desired file format
7. Save of file in given output path
8. Information for user about restult
                
 *In case of error during any of algorithm steps user gets proper error information and exception is thrown*

---
# Installation, Usage

In order to run the tool Java Runtime Environment 8+ is required
Tool can be triggers via running main class or impemented into existing solution and used calling Converter object with params

---
# Tech stack, data format, program structure

- Project is developed using Java language as a console application.
- Version control via Git&GitHub
- All the external libraries are described below
- Acceptable input file formats are: 
-- .json,
-- .xml,
-- .yaml,
-- .csv
- Content of files has to match their specification
- Program will not accept any other file extensions
- Output file extension is chosen by the user from ones listed above
---
# Structre diagram

![alt text](https://raw.githubusercontent.com/ssysek/converter/master/diag/classuml.png?raw=true)

---
# Test plan
### Unit tests:
- validation of file path
- validation of file extension
- validation of file structure
- conversion of each file structure subelements
- I/O operations, file save, file read
---
# Possible extensions
- Export as a library and possibility to use in another projects
- Creation an application containing GUI, allowing user to select the input file, location of the output file and the output file format in an intuitive way, even via drag&drop
- Support more file extensions
---
## External libraries

- Apache Commons IO - used for file comparision in tests
- JUnit 5, JUnit Jupiter - used for testing of application 

___

[Google docs documentation](https://docs.google.com/document/d/1ENno_0kCY5ld6gknKiRATNfy-hiUUyqH0_rWKdvlXSA)

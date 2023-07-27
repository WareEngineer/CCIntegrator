# CCIntegrator

## Contents
1. Summary
2. Tool Introduction
3. Installation
4. Directories Within This Tool
5. User Guide
<br><br>

## 1. Summary
- ### Goal:
  CCIntegrator provides a starting point for systematic product development by rebuilding a code base and platform that can be reused in multiple products developed Clone-and-Own approach.

- ### Problem Definition: 
  When you want to rapidly develop similar products that meet the subtly different requirements of specific customers, Clone-and-Own (CAO) is a simply applicable way to develop such a product family. In the CAO approach, product development is achieved by duplicating a previously developed product and modifying some of it to meet different requirements. The CAO approach is widely used in the product family development because of its low initial development cost. As product development based on CAO continues for a long period of time, the number of products that need to be maintained independently increases. In the end, the difficulty in product management also increases due to clone codes that were subtly different each other.

- ### Approach:
  Given products developed in the CAO approach, CCIntegrator rebuilds a reusable code base and platform by identifying and unifying clone codes from the source files of them. In addition, the code base and platform support the regeneration of existing products without any errors. As a result, we can do integrated management of existing products.
<br><br><br>

## 2. Tool Introduction
In the process of rebuilding the code base and platform, the series of tasks performed by CCIntegrator are as follows:

### Concept for CCIntegrator

![Concept](https://github.com/WareEngineer/CCIntegrator/assets/24680469/f165057f-b7bd-4215-882d-aec3ff8319f0)
<br><br>

### Workflow in CCIntegrator

![Workflow](https://github.com/WareEngineer/CCIntegrator/assets/24680469/9f22f709-fa08-46e5-a98a-1ed025501daf)
<br><br>

### Phase 1. Determining a product family  
#### In this step, functionally similar products are identified from a product family given as input, and products suitable for platform construction are selected in consideration of reusability of the code base to be created as a result. The detailed tasks related to this are listed below:
* Find and remove unused dead code in each product.  
* Measure functional commonality of each product in terms of the set of features that make up the product family.  
* Assuming an expected reusability of platform that are rebuilt from the product family.  
* If the expected reusability is less than a criterion, sequentially remove products with the lowest functional commonality from the product family.
<br><br>

### Phase 2. Clustering clone codes  
#### In this step, collects clone codes that might be able to have been copied from other products, and defines each set of clone codes as a clone cluster. The detailed tasks related to this are listed below:
* For each method signature constituting the product family, the source files including the method signature are grouped as a candidate cluster. Note that the candidate clusters might not be disjoint each other. Therefore, it is necessary to determine the appropriate clone clusters by considering code similarity and reusability.
* Apply the same coding style to all source codes in the product family. A single coding style always guarantees the same representation of a set of tokens, so code similarity calculation can be efficiently performed through line-by-line comparison. 
* If the code similarity is greater than equal to a criterion, candidate cluster that can be reused in more products are preferentially selected as clone clusters.
* If the selected candidate cluster includes some source files that have been handled other clone clusters, the code similarity is recalculated after excluding those source files from the candidate cluster.
<br><br>

### Phase 3. Merging clone codes
#### In this step, common lines that appear in all products and variable lines that appear in some products are extracted from the clone clusters. A reusable code base is created by merging common and variable lines. In the code base, variable lines are inserted between common lines with annotations listing product identifiers as active conditions. Given a product identifier, the variable lines can be activated or deactivated by the preprocessor to match a specific product. The detailed tasks related to this are listed below:
* Discover common and variable lines from clone clusters by applying LCS (Longest Common Subsequence) algorithm.
* Group consecutive variable lines in each of source files into a code block.
* Wrap the block in an annotation with active conditions and insert it between common lines.
* If code blocks with the same content appear repeatedly in the same location, only a new active condition is listed in the existing annotation rather than adding a new annotation.
<br><br>

### Phase 4. Building a tracing model
#### In this step, a tracing model for variable code blocks is built based on the source files and related product information used in the process of building the code base. The tracing model provides information about which code blocks should be activated or deactivated in the process of recreating an existing product based on the code base. It also provides information about which files this preprocessed source code should fill in. The detailed tasks related to this are listed below:
* For each clone cluster used in building a code base, extract a file path and a product identifier related the clone cluster.
* Build a tracing model by mapping file paths to product identifiers. In the product creation process, the code base preprocessed by the preprocessor is realized as specific files for a particular product based on the tracing model.
<br><br>

### Phase 5. Generating a product
#### In this step, CCIntegrator supports error-free regeneration of existing product variants based on a previously established code base and tracing model.
<br><br>

## 3. Installation
### This section introduces how to import CCIntegrator into Eclipse.
1. Copy the Web URL for this GitHub repository.

![Installation-1](https://github.com/WareEngineer/CCIntegrator/assets/24680469/47bc925e-31ab-44af-80ef-741b9ab3739b)

2. Run Eclipse.
3. Click 'File' in the top menu of Eclipse IDE, and select 'Import...' in the submenu.

![Installation-3](https://github.com/WareEngineer/CCIntegrator/assets/24680469/918771cd-b3e7-4e7c-84d3-14a5c33cede4)

4. Type ‘git’ and select ‘Projects from Git’.

![Installation-4](https://github.com/WareEngineer/CCIntegrator/assets/24680469/c943347e-9409-4518-8a2d-d5478f659cbf)

5. Select ‘Clone URI’.

![Installation-5](https://github.com/WareEngineer/CCIntegrator/assets/24680469/9edb758b-56f5-4b52-a4a7-98e9126afec4)

6. Paste the Web URL you copied from the GitHub repository.

![Installation-6](https://github.com/WareEngineer/CCIntegrator/assets/24680469/07de6ecf-1219-4d04-a798-6dee17b4e5a3)

7. Press the ‘Next’ button successively in the menu that follows, then CCIntegrator will be downloaded from the Github repository.

![Installation-7](https://github.com/WareEngineer/CCIntegrator/assets/24680469/2ee58594-824c-447a-93e9-554514bcb10d)

8. Finally, after clicking the ‘Finish’ button, you can see that CCIntegrator has been successfully imported into the Eclipse IDE.

![Installation-8](https://github.com/WareEngineer/CCIntegrator/assets/24680469/86eb7797-0c25-4038-8e11-67ece51eb89f)

<br><br><br>

## 4. Directories Within This Tool
### The CCIntegrator project consists of 5 main directories: *examples*, *products*, *repository*, *src*, and *temp*.
![Directories](https://github.com/WareEngineer/CCIntegrator/assets/24680469/26aaca6c-7124-4b4d-b36f-00b296b9f2bc)
* *src* : It consists of Java source files that implement CCIntegrator's functions.
* *examples* : This directory contains examples of several product families so that tool users can easily experiment with CCIntegrator’s features and see the results.
* *temp* : This is the directory where a clone of the original product family is stored so that the original product family is not damaged. In the process of building the code base and platform, The CCIntegrator performs all operations on the clone.
* *repository* : This directory stores a code base generated as a result executing CCIntegrator. Cross-product clone code identified from a product family is merged into a code base that can be reused across multiple products and stored in this directory.
* *products* : This directory stores generated product variants when a user demands for a particular product variant can be generated on the basis of the tracing model.
<br><br><br>

## 5. User Guide
### Guide 1. Building a code base and platform

When you run CCIntegrator, you can see the following menu.

![UserGuide1-1](https://github.com/WareEngineer/CCIntegrator/assets/24680469/489a10b4-ac1c-49ff-b014-69beaea368fd)

If you want to build a code base and platform for a product family, first prepare a product family developed in the Java language and select number 1 from the menu. With the cursor on the prompt active, press number 1 on the keyboard and type Enter.

![UserGuide1-2](https://github.com/WareEngineer/CCIntegrator/assets/24680469/9012fafa-9410-432d-88d7-13abf0fd190b)

It's okey if you don't prepare your own product family.

CCIntegrator/examples directory contains four product families developed in java language:  ArgoUML, ApoGames, Elevator, and HelloWorld.

  -	ArgoUML: https://github.com/but4reuse/argouml-spl-benchmark
  -	ApoGames: https://variability-challenges.github.io/2018/ApoGames/index.html
  -	Elevator and HelloWorld: https://www.featureide.de/index_tool.php

In addition to these examples, there are four text files to be used as input to the tool: argoUML.txt, apoGames.txt, elevator.txt, and helloWorld.txt. Each text file lists the project root path of the product variants that make up the product family.

![UserGuide1-3](https://github.com/WareEngineer/CCIntegrator/assets/24680469/44c579fe-8117-408a-89e8-129641f38752)

This guide explains the subsequent process with ApoGames example. If you want to apply it to your own product family, you should create a text file that lists the root path of the product variants. And type the path of the text file on the Command Prompt. Given as input a text file listing root paths for product variants, the following basic operations are performed automatically.

* CCIntegrator sequentially reads the root directory of product variants from the given text file and copies that directory to a temporary directory ‘CCIntegrator/temp’.
* CCIntegrator scans all files for product variants and removes bytecode files with the file extension 'class'.
* When a single source file defines several top-level classes, each top-level class is saved a source file by being named the class name in the same package. As a result, one source file has only one top-level class.
* For syntax analysis, use javaparser (https://javaparser.org/) to parse Java source files.

![UserGuide1-4](https://github.com/WareEngineer/CCIntegrator/assets/24680469/90d00d9d-5c29-4ce7-b5ca-a4cdc726f2a9)

When the previous operations are completed for each product variant, CCIntegrator begins identifying dead code in each product variant. Dead code is the source code that is not related to the operation of the program, and it is determined by tracking the reference relationship between source files from a program entry point. In the Java language, a ‘main()’ function is used as a program entry point, and the Java language allows multiple main() function definitions within a project. Therefore, if there are multiple classes defining a ‘main()’ function in a project, you must manually select one of them.

![UserGuide1-5](https://github.com/WareEngineer/CCIntegrator/assets/24680469/bd16ce60-5e52-4173-9aa0-7d2bfd7b2f24)

After eliminating dead code, CCIntegrator begins selecting functionally similar product variants from a given initial family. When code bases and platforms are built from functionally similar product variants, they effectively support managing and generating the product family. To build your code base and platform, you need to set up filtering criteria so that product families are determined based on a cross-similarity for each product variant.

![UserGuide1-6](https://github.com/WareEngineer/CCIntegrator/assets/24680469/b561365a-dec2-47ac-8d5c-d197ba789f4e)

Next, the CCIntegrator uniformly applies a single coding style to all Java source files that make up the previously determined product family. Line-by-line comparison between source code is an efficient way to quantify how many elements are commonly reused in a set of source files. However, in the line-by-line comparison, even source codes composed of the same tokens may be judged as unequal codes due to coding style. When all source codes are written in a single coding style, line-by-line comparison can be performed more accurately.

![UserGuide1-7](https://github.com/WareEngineer/CCIntegrator/assets/24680469/086f84b4-e0ff-4ad8-af5f-d874558b387e)

When a developer sometimes copies source code developed in an existing product to another product for reuse, a clone cluster is a set of original source code and cloned source codes. It is inefficient to determine appropriate clone clusters after examining whether all combinations that can be created from all source codes constituting the product family are clone clusters. On the other hand, source files with the same relative path and name from the root directory can be classified as a clone cluster, but this causes a situation in which the cloned source code is missing from the clone cluster due to differences in the path.

For each method signature, CCIntegrator assembles the source files containing that method signature and defines it as a candidate for a clone cluster. Then, CCIntegrator calculates the similarity between the source codes belonging to each candidate and selects the candidate whose similarity is higher than the standard value as a clone cluster. To determine a clone cluster among candidates, you need to set a baseline for the degree of similarity between source codes within a clone cluster. In general, when the reference value is set high, the code base to be generated is reused only in a small number of products with high similarity to each other, and the complexity of the code base itself is reduced by minimizing the parts that need to be handled variably according to variations.

![UserGuide1-8](https://github.com/WareEngineer/CCIntegrator/assets/24680469/75ffa7cc-cfe9-442d-a0bb-edbfdc5a1f0c)

If all clone clusters are determined, CCIntegrator builds a code base by integrating the source codes in each clone cluster into one. In order to extract common lines from clone codes, a method applying LCS (Longest Common Subsequence) algorithm is used. Based on the extracted common lines, variable lines are identified in each clone code. For the identified variable lines, annotations are used to mark the parts to be activated according to specific conditions. As a result, you can manage your product family with a single code base.

![UserGuide1-9](https://github.com/WareEngineer/CCIntegrator/assets/24680469/eb7232d1-b930-461b-9774-43eb5cdfdabe)

Along with building the code base, CCIntegrator constructs a tracing model. In the process of building a code base, variable codes are marked with annotations by giving unique identifiers. The tracing model associates an identifier assigned to a variable code with each product variant in a product family. It supports the variable code of the code base to be processed by the preprocessor so that a code base can represent any variants within a product family. The code base and tracing model for the product family are created in the 'CCIntegrator/repository' directory.

![UserGuide1-10](https://github.com/WareEngineer/CCIntegrator/assets/24680469/640013a3-a441-41ca-ada8-f3148cbdba58)

In addition to source files, software products may have other files that are required to run the system but are not source code. These files are categorized by related product and stored in the non-sourcecode directory, which is a subdirectory of the repository.

![UserGuide1-11](https://github.com/WareEngineer/CCIntegrator/assets/24680469/368b9427-023b-49ac-a120-311969d60193)

A code base are stored in the CCIntegrator/repository/sourcecode directory in the form of a text file. Text files are created as many as the number of clone clusters.

![UserGuide1-12](https://github.com/WareEngineer/CCIntegrator/assets/24680469/dbf96078-6901-4d1e-b135-7ac77590f296)

The tracking model specifies information about source and non-source files required by each product variant, and is written in xml format.

![UserGuide1-13](https://github.com/WareEngineer/CCIntegrator/assets/24680469/a94fea30-f06a-4bec-b845-2ec300305686)

The 'suggestion_for_refactoring.txt' file records cases with different file names when the same code base is reused in product variants. If refactoring is performed considering the proposal, a solid platform can be built by securing the structural equality of the reused code base.

![UserGuide1-14](https://github.com/WareEngineer/CCIntegrator/assets/24680469/450a4fef-63d8-4f1f-b4ac-8c261fa0cd8b)

<br><br>

### Guide 2. Generating product variants based on a code base and platform
When you run CCIntegrator, you can see the following menu.

![UserGuide2-1](https://github.com/WareEngineer/CCIntegrator/assets/24680469/08f7a163-06fc-4e9e-841e-0bb6b6ff1009)

You have to build the code base and platform through Guide 1 before generating product variants. If the code base and platform construction for the product family has been completed through the tool guide 1, product variants can be generated based on them. To generate a product variant, select number 2 from the menu. With the cursor on the prompt active, press number 2 on the keyboard and type Enter.

![UserGuide2-2](https://github.com/WareEngineer/CCIntegrator/assets/24680469/4bfda5d1-186d-4909-b2a1-f8c781f8683c)

When CCIntegrator displays a list that can be generate from the code base, please select the product you want to create. If you want to end product generation and return to the menu, press 0.

![UserGuide2-3](https://github.com/WareEngineer/CCIntegrator/assets/24680469/94646ef5-1d1c-4673-8191-4303a33a12f3)

CCIntegrator refers to model.xml and creates the product selected by the user. In this process, the annotation part of the code base is preprocessed to suit the selected product. And the preprocessed code is mapped to specific files required by each product variant. As a result, the generated product is stored in the 'CCIntegrator/products' directory.

![UserGuide2-4](https://github.com/WareEngineer/CCIntegrator/assets/24680469/2e200273-40fa-47ea-a9ea-869f1853bd57)

When you import the created product in the ‘CCIntegrator/products’ directory into Eclipse, you can see that the product runs without errors.

![UserGuide2-5](https://github.com/WareEngineer/CCIntegrator/assets/24680469/9f62c817-cbb6-41c5-8772-b2864494014b)

# Guesser Agent for the ESSENCE Taboo Challenge

Taboo is a word-guessing game in which one player has to describe a target term to another player by giving hints that are neither the target term nor other terms specified in a predetermined list of taboo words. The Location Taboo (LT) Challenge, which has been proposed by the ESSENCE Marie Curie Initial Training Net- work, is a version of Taboo that only contains cities as target terms and is intended to be played by artificial guesser agents. This repository provides the code of a Guesser agent presented in a [workshop paper](https://github.com/keminus/GuesserAgent#publication). This readme will guide you in the installation of the agent.

## Presentation of the Taboo Game

Taboo is a word-guessing game in which one player has to describe a target term to another player by giving hints that are neither the target term nor other terms specified in a predetermined list of taboo words. For example, a player might have to describe water without using sea, blue or beverage.

The Location Taboo Challenge (LTC), which has been proposed by the [ESSENCE Marie Curie Initial Training Network](https://www.essence-network.com/), is a version of Taboo that only contains cities as target terms and is intended to be played by artificial guesser agents. In the LTC, the hints, which are words associated to the target city, are sequentially provided to the guesser agent, and the goal is to guess the target location as soon as possible.

## Prerequisites

This project has been developed using [Eclipse](https://eclipse.org/). The repository is a standard eclipse project folder. Since the code is written in [Java](https://www.java.com/fr/), any other IDE and it can be executed from a terminal in any operating system that has Java installed. However, the code has only been tested in the Eclipse environment so we recommend you to use it.

### Java

Download the [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) version compatible with your OS and run the installation.

### Eclipse

You can download Eclipse [here](https://eclipse.org/downloads/) for a large panel of operating systems. A good tutorial for the installation is provided by the Eclipse [Wiki](https://wiki.eclipse.org/Eclipse/Installation). You should download the last Eclipse IDE for Java Developers version of the IDE.

## Installation

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Import the repository files in Eclipse

Copy your local repository folder in your current workspace. If this is the first time that you launch Eclipse, it will ask you to create a workspace. Note its direction and copy the local repository there.

In the window that pops up, select Java Project and clic next.

In the Project name field, enter 'ArgumentationOnMeaning' (or the name of the local folder of this repository if you changed it).

Clic on Finish. The Package Explorer of Eclipse (left side by default) should look like this:

### Place the complementary files in the Eclipse project

Two files need to be added to the project in order to run the guesser agent. They are not provided in the repository.

```
cities.tsv
Taboo_cleaned_onlyPerfects.csv
```

If you want to modify these names, see the Javadoc of the classes from the Tester package.

cities.tsv is a spreadsheet file with columns separated by tabulations. This file contains informations about the cities that the guesser knows about. Each line correspond to a different city. The file has six columns, respectively:

```
1- a unique identifier for the city
2- the name of the city name
3- the name of the city's country
4- the latitude of the city
5- the longitude of the city
6- a list taboo words for the city, each separated by a semicolon
```

example:

```
1	Hiroshima	Japan	3.438.520.300	13.245.529.300	atomic bomb; castle; park; humid; gardens; rebuilt; coastline; war; pancake; shrine; cherry
```

Taboo_cleaned_onlyPerfects.csv is a spreadsheet file with columns separated by commas. This file contains a set of recorded Taboo games, in order to train the guesser. Each line figures a different turn from a Taboo game, with the message exchanged during this turn. The file has ten columns, respectively:

```
1- a unique identifier for the game
2- the city's unique identifier (similar to the one in cities.tsv)
3- the city's name
4- blank column
5- "guesser" or "describer", depending on which is playing during the turn
6- a string with the message sent to the other player
7- a list taboo words for the city, each separated by a semicolon
8- a unique identifier for this message
9- the name of the user account that sent the message
10- the date of the message
```

The file should start with the header:

```
game_id , city_id , city_name , FLAG , role , message , tabooWords , message_id , user_name , mes_time
```

After the header, each line is built on the following model:

```
9475366 , 25 , Barcelona , , describer , No. sea , flamenco; architecture; art; gardens; football; waterfront; dance; cathedral; culture; independent; rich; autonomous , 555 , nschneid , 18.05.2016 15:52:45
```

## Running the tests


## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Publication

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc

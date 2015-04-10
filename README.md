Cafe4Maude: A translation from CafeOBJ into Maude
=================================================

Cafe4Maude is a tool to introduce CafeOBJ specifications into the Maude system.
This tool has the following features:
* Supports operators, predicates, equations, and transitions, and all their related
attributes.
* Supports open-close environments.
* Includes the predefined _=_ operator and the corresponding equations.
* Full integration with Maude tools (such as the model checker or the citp).


The current translation has a number of limitations:
* Behavioral specifications are not supported.
* The CafeOBJ search predicate cannot be used (the Maude one can be used, though).
* The _==>_ predicate is not supported.

Getting the tool
----------------

The code of 'Cafe4Maude' is contained in a GIT repository on GitHub, whose URL is
https://github.com/ariesco/cafe4Maude. To get a copy of the repository you only
have to write in your Linux/MacOS console:

    $ git clone https://github.com/ariesco/cafe4Maude

This will create a cafe4Maude folder containing the source code of the tool as well as
several examples.

Using the tool
--------------

1. In order to use the easiest way it to configure the **cafe2maude** script by
editing the location of the Maude binary in the variable *MAUDE*. It is assumed that
the Full Maude file is accessible in the folder where Maude is located or in the current
folder.

Once this information is set, CafeOBJ specifications can be loaded into the Maude system.
For example, you can execute the alternating bit protocol example available in the
examples folder by typing:

    $ ./cafe4maude examples/abp.cafe

Once a module has been loaded, Maude commands can be used in the usual way.
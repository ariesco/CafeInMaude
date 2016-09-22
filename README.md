Cafe4Maude: A translation from CafeOBJ into Maude
=================================================

Cafe4Maude is a tool to introduce CafeOBJ specifications into the Maude system.
This tool has the following features:
* Supports operators, predicates, equations, and transitions, and all their related
attributes.
* Supports open-close environments.
* Supports the predefined EQL and RWL modules.
* It provides the CafeInMaude Proof Assistant (CiMPA) for performing inductive proofs.
* It provides the CafeInMaude Proof Generator (CiMPG) for inferring proof scripts
from proof scores.
* Full integration with Maude tools (such as the model checker or the citp).

The current translation has a number of limitations:
* Behavioral specifications are not supported.
* Importation between modules with loose and tight semantics are limited to
those supported by Maude.

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

The CafeInMaude Proof Assistant (CiMPA)
---------------------------------------

The CafeInMaude Proof Assistant allows users to prove properties on their CafeOBJ
specifications. The available commands, that must be used inside open-close
environments, are:
* :goal(EqS). This command introduces a new goal composed of the equations in EqS.
When this command is used the previous proof is deleted.
* :ind on (V). This command indicates that induction will be carried out on the
variable V.
* :def LAB = :ctf{Eq}. This command maps the label LAB to the equation Eq, so it can
be later used for case distinction.
* :def LAB = :ctf[T]. This command maps the label LAB to the term T, so it can
be later used for case distinction.
* :apply(si). This command applies simultaneous induction on the variable V set with
command :ind to the current goal. Given the sort S of V, it generates as many new goals
as the numbers of constructors defined for S (including subsort relations) in the
current module.
* :apply(tc). This command applies the theorem of constants to the current goal.
It generates as many new goals as sentences stated in the goal.
* :apply(rd). This command reduces the current goal by means of equations. If all the
sentences in the goal are reduced to true then the goal is proven. This command
substitutes the current goal by a new one with the sentences reduced.
* :apply(rd-). This command reduces the current goal by means of equations. If all the
sentences in the goal are reduced to true then the goal is proven. If the goal is not
proven the goal remains unchanged.
* :apply(LAB). This command uses the equation or term associated to LAB for case
distinction in the current goal. If it is an equation it generates two new goals,
the first one stating the equation holds and the second one stating the equation does
not hold. If it is a term with sort S it generates as many new goals as constructors
are defined for S in the current module; each goal states that the term is equal to
a specific constructed term.
* **:imp[LAB] .**

  op :imp[_]. : @CafeToken@ -> @CafeInductiveComm@ [ctor] .
  op :imp[_]by{_} : @CafeToken@ @NeCafeTokenList@ -> @CafeInductiveComm@ [ctor] .
  op :show`proof : -> @CafeInductiveComm@ [ctor] .
  op :desc`proof : -> @CafeInductiveComm@ [ctor] .
  op :desc`. : -> @CafeInductiveComm@ [ctor] .

* :sel(G). This command selects the goal G as next one.
* :postpone .. This command postpones the current goal, so it will not be tried
again until the rest of goals has been dealt with.

Using the CITP
--------------

The code here includes the last version of the translation integrated with the
Constructor-based Inductive Theorem Prover. The latest version of the CITP is
available [here](http://www.jaist.ac.jp/~danielmg/citp.html).

Using the MFE
-------------

The code here includes the last version of the translation integrated with the
Maude Formal Environment. The latest version of the MFE is available
[here](http://maude.lcc.uma.es/MFE/).

Using the MDDTCG
----------------

The code here includes the last version of the Maude Declarative Debugger and
Test-Case Generator. More details are available
[here](http://maude.sip.ucm.es/debugging/) for the declarative debugger and
[here](http://maude.sip.ucm.es/testing/) for the test-case generator.
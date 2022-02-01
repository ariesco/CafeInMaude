CafeInMaude: A translation from CafeOBJ into Maude
==================================================

CafeInMaude is a tool to introduce CafeOBJ specifications into the Maude system. Please note
that the current version of CafeInMaude runs in Maude alpha132 (December 2020), please visit
the *previous versions* folder for Maude 2 versions (note older versions do not support most
of the features, like parallel execution).

This tool has the following features:
* Supports operators, predicates, equations, and transitions, and all their related
attributes.
* Supports open-close environments.
* Supports the predefined EQL and RWL modules.
* It provides the CafeInMaude Proof Assistant (CiMPA) for performing inductive proofs.
* It provides the CafeInMaude Proof Generator (CiMPG) for inferring proof scripts
from proof scores.
* It provides the CafeInMaude Proof Generator & Fixer-Upper (CiMPG+F) for generating
proofs.
* It supports multi-core computation.

The current translation has a number of limitations:
* Behavioral specifications are not supported.
* Importation between modules with loose and tight semantics are limited to
those supported by Maude.

Getting the tool
----------------

The code of 'CafeInMaude' is contained in a GIT repository on GitHub, whose URL is
https://github.com/ariesco/CafeInMaude. To get a copy of the repository you only
have to write in your Linux/MacOS console:

```
    $ git clone https://github.com/ariesco/CafeInMaude
```

This will create a CafeInMaude folder containing the source code of the tool as well as
several examples.

Using the tool
--------------

1. The tool is started by just loading the **CafeInMaude.maude** file into the Maude system:

```
    $ maude -allow-files src/cafeInMaude.maude
```

Once the tool is started, the **load** command can be used for loading files. For example, we can load
the 2-processes mutal exclusion protocol specification by typing:

```
    $ CafeInMaude> load ../examples/CCiMPG/2p-mutex/2p-mutex.cafe .
```

Likewise, we would load the proof related to this protocol with the following command:

```
    $ CafeInMaude> load ../examples/CCiMPG/2p-mutex/all_proofs.cafe .
```

Finally, we can set the file for storing proofs as follows:

```
    $ CafeInMaude> set-output ../examples/CCiMPG/2p-mutex/2p-proof.cafe .
```

Because CiMPG+F supports parallel execution, we need to set the number of cores that
will be used for parallel computation by:

```
    $ CafeInMaude> set-cores 4 .
```

Because the open-close environments are named as **inv1** in the proofs, we can infer
the proof using:

```
    $ CafeInMaude> :infer-proof inv1 .
```

The proof can be stored in an external file by typing:

```
    $ CafeInMaude> :save-proof .
```

This proof can be loaded using:

```
    $ CafeInMaude> load ../examples/CCiMPG/2p-mutex/2p-proof.cafe .
```

In general, the examples in the **CCIMPG** folder provide a **commands_cimpg.cafe** file with the
commands required to run the example using proof scores. For example, the **2p-mutex** folder contains a
**commands_cimpg.cafe** file that can be loaded as follows:

```
    $ CafeInMaude> load ../examples/CCiMPG/2p-mutex/commands_cimpg.cafe .
```

Likewise, we include a **commands_generate.cafe** file with the commands for running the example and inferring
the proof without proof scores. For example, the **2p-mutex** folder contains a
**commands_generate.cafe** file that can be loaded as follows:

```
    $ CafeInMaude> load ../examples/CCiMPG/2p-mutex/commands_generate.cafe .
```

These files contain the commands for loading the modules, generating the proof, saving, and loading it,
explained above.

General commands
----------------

The following commands can be used for any tool:
* **load PATH .**. This command executes the input stored in the file located at **PATH**.
* **set-cores N .**. This command indicates that **N** processes can be used for concurrent computation.
* **set-output PATH .**. This command sets **PATH** as the default output.
* **:save-proof .**. This command stores the current proof in the text file previously indicated by means of
**set-output**.

The CafeInMaude Proof Assistant (CiMPA)
---------------------------------------

The CafeInMaude Proof Assistant allows users to prove properties on their CafeOBJ
specifications. The available commands, that must be used inside open-close
environments, are:
* **:goal(EqS)**. This command introduces a new goal composed of the equations in
**EqS**. When this command is used the previous proof is deleted.
* **:ind on (V)**. This command indicates that induction will be carried out on the
variable **V**.
* **:def LAB = :ctf{Eq}**. This command maps the label **LAB** to the equation **Eq**,
so it can be later used for case distinction.
* **:def LAB = :ctf[T]**. This command maps the label **LAB** to the term **T**, so
it can be later used for case distinction.
* **:apply(si)**. This command applies simultaneous induction on the variable V set with
command :ind to the current goal. Given the sort S of V, it generates as many new goals
as the numbers of constructors defined for S (including subsort relations) in the
current module.
* **:apply(tc)**. This command applies the theorem of constants to the current goal.
It generates as many new goals as sentences stated in the goal.
* **:apply(rd)**. This command reduces the current goal by means of equations. If all the
sentences in the goal are reduced to true then the goal is proven. This command
substitutes the current goal by a new one with the sentences reduced.
* **:apply(rd-)**. This command reduces the current goal by means of equations. If all the
sentences in the goal are reduced to true then the goal is proven. If the goal is not
proven the goal remains unchanged.
* **:apply(LAB)**. This command uses the equation or term associated to **LAB** for
case distinction in the current goal. If it is an equation it generates two new goals,
the first one stating the equation holds and the second one stating the equation does
not hold. If it is a term with sort S it generates as many new goals as constructors
are defined for S in the current module; each goal states that the term is equal to
a specific constructed term.
* **:imp[LAB] .**. This command takes the equation identified by the label **LAB**,
which must be defined in the current module and must have **true** as righthand side,
and generates a new goal where the lefthand side of the equation implies the former
goal.
* **:imp[LAB] by {V0 <- T0 ; ... Vn <- Tn} .**. This command takes the equation
identified by the label **LAB**, which must be defined in the current module and
must have **true** as righthand side, and generates a new goal where the lefthand
side of the equation, once each variable **Vi** is substituted by the term **Ti**,
implies the former goal.
* **:sel(G)**. This command selects the goal G as next one.
* **:postpone .**. This command postpones the current goal, so it will not be tried
again until the rest of goals has been dealt with.
* **:show proof**. This command displays the current goals (that is, the leaves of
the proof tree). A goal is displayed with * if it has already been proved and with
**>** if it is the current one.
* **:desc .**. This command displays the current goal.
* **:desc proof**. This command depicts the complete proof tree.
A goal is displayed with * if it has already been proved and with
**>** if it is the current one.

The CafeInMaude Proof Generator (CiMPG) & the CafeInMaude Proof Generator & Upper-Fixer (CiMPG+F)
-------------------------------------------------------------------------------------------------

The CafeInMaude Proof Generator provides annotations for inferring proof scripts for
CiMPA from proof scores. These annotations are:
* **:id(LAB)**, which indicates that the proof scores is associated to the proof for
goal **LAB**.
* **:infer-proof LAB .**, which asks CiMPG to generate the proof script for goal **LAB** by
using the proof scores previously associated to this proof by means of **:id**.
* If some open-close environments are missing, then CiMPG+F is used.

CiMPG requires that:
* All open-close environments open the same module.
* All the reductions on these environments are related to the proof.

Using the MFE (discontinued)
----------------------------

The code here includes the last version of the translation integrated with the
Maude Formal Environment. The latest version of the MFE is available
[here](http://maude.lcc.uma.es/MFE/).

Using the MDDTCG (discontinued)
-------------------------------

The code here includes the last version of the Maude Declarative Debugger and
Test-Case Generator. More details are available
[here](http://maude.sip.ucm.es/debugging/) for the declarative debugger and
[here](http://maude.sip.ucm.es/testing/) for the test-case generator.

Acknowledgments
---------------

Research partially supported by Japanese projects *Kakenhi* 23220002 and 19H04082,
MICINN Spanish project *StrongSoft* (TIN2012-39391-C04-04), and
Comunidad de Madrid project *N-Greens* Software-CM (S2013/ICE-2731).
Running NSLPK
=============

This folder contains the following files:
* **nslpk.cafe**, which contains the specification of the NSLPK protocol.
* **all_proofs.cafe**, which contains the proof score for verifying the NSLPK protocol. They can be introduced into the system and used to infer a CiMPA proof by using CiMPG.
* **nslpk-proof-generate_inv1.cafe**, which contains the CiMPA script automatically generated for the first invariant. The proof assumes the second and the third properties have been proved beforehand and can be used.
* **nslpk-proof-generate_inv2.cafe**, which contains the CiMPA script automatically generated for the second invariant. Note that this invariant is independent from the rest of the proof.
* **nslpk-proof-generate_inv3.cafe**, which contains the CiMPA script automatically generated for the third invariant. Note that this invariant is independent from the rest of the proof.
* **commands_generate_with_proven.cafe**, which contains the CiMPG+F commands for generating **nslpk-proof-generate_inv1.cafe**, **nslpk-proof-generate_inv2.cafe**, and **nslpk-proof-generate_inv3.cafe**.
* **nslpk-proof-generate.cafe**, which contains the CiMPA script automatically generated for the three invariants.
* **commands_generate.cafe**, which contains the CiMPG+F commands for generating **nslpk-proof-generate.cafe**.
* **nslpk-proof.cafe**, which contains the CiMPA script generated for the three invariants by using the proof score in **all_proofs.cafe**.
* **commands_cimpg.cafe**, which contains the CiMPG+F commands for generating **nslpk-proof.cafe**.

Starting the tool and loading NSLPK
-----------------------------------

As explained in the main page, the tool is started by typing:

```
    $ maude -allow-files src/cafeInMaude.maude
```

Once the tool is started, we can introduce modules and try to verify proofs. All proofs in the next subsections will require to load the specification, which is done by typing:

```
    $ CafeInMaude> load ../examples/CCiMPG/NSLPK/nslpk.cafe .
```

Generating a proof script using proof scores
--------------------------------------------

As indicated above, the commands for generating the proof script in this case are available in **commands_cimpg.cafe**. Although it would be enough to load this file (the load required to load it is included at the end of the file), we describe the commands that have been used.

We first need to

```
load ../examples/CCiMPG/NSLPK/all_proofs.cafe .
```

```
set-cores 4 .
```

```
set-output ../examples/CCiMPG/NSLPK/nslpk-proof.cafe .
```

```
:infer-proof nslpk .
```

```
:save-proof .
```

```
load ../examples/CCiMPG/NSLPK/nslpk-proof.cafe .
```

Inferring a proof script assuming invariants are mutually dependent
-------------------------------------------------------------------

Inferring a proof script
------------------------
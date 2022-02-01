Running NSLPK
=============

This folder contains the following files:
* **nslpk.cafe**, which contains the specification of the NSLPK protocol.
* **all_proofs.cafe**, which contains the proof score for verifying the NSLPK protocol. They can be introduced into the system and used to infer a CiMPA proof by using CiMPG.
* **nslpk-proof-generateinv1.cafe**, which contains the CiMPA script automatically generated for the first invariant. The proof assumes the second and the third properties have been proved beforehand and can be used.
* **nslpk-proof-generate_inv2.cafe**, which contains the CiMPA script automatically generated for the second invariant. Note that this invariant is independent from the rest of the proof.
* **nslpk-proof-generate_inv3.cafe**, which contains the CiMPA script automatically generated for the third invariant. Note that this invariant is independent from the rest of the proof.
* **commands_generate_with_proven.cafe**, which contains the CiMPG+F commands for generating **nslpk-proof-generateinv1.cafe**, **nslpk-proof-generateinv2.cafe**, and **nslpk-proof-generateinv3.cafe**.
* **nslpk-proof-generate.cafe**, which contains the CiMPA script automatically generated for the three invariants.
* **commands_generate.cafe**, which contains the CiMPG+F commands for generating **nslpk-proof-generate.cafe**.
* **nslpk-proof.cafe**, which contains the CiMPA script generated for the three invariants by using the proof score in **all_proofs.cafe**.
* **commands_cimpg.cafe**, which contains the CiMPG+F commands for generating **nslpk-proof.cafe**.

Generating a proof script using proof scores
--------------------------------------------

As indicated above, the commands for generating the proof script in this case are available in **commands_cimpg.cafe**. Although it would be enough to load this file

Inferring a proof script assuming invariants are mutually dependent
-------------------------------------------------------------------

Inferring a proof script
------------------------
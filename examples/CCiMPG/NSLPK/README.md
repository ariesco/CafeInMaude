Running NSLPK
=============

This folder contains the following files:
* *nslpk.cafe*, which contains the specification of the NSLPK protocol.
* *all_proofs.cafe*, which contains the proof score for verifying the NSLPK protocol. They can be introduced into the system and used to infer a CiMPA proof by using CiMPG.
* *nslpk-proof-generate_tosem_inv1.cafe*, which contains the CiMPA script automatically generated for the first invariant. The proof assumes the second and the third properties have been proved beforehand and can be used.
* *nslpk-proof-generate_tosem_inv2.cafe*, which contains the CiMPA scrpt automatically generated for the second invariant. Note that this invariant is independent from the rest of the proof.
* *nslpk-proof-generate_tosem_inv3.cafe*, which contains the CiMPA scrpt automatically generated for the third invariant. Note that this invariant is independent from the rest of the proof.
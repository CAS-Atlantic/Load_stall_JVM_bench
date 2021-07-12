# Load_stall_JVM_bench

This repository contains the code of the stall-focused benchmarks for JVMs on the x86 platforms. Created by Zhuoran Li (zhuoran.li@unb.ca) in *Project 1039 - Load Stall Minimization* for the Centre for Advanced Studies - Atlantic (CAS-Atlantic) at the Univerity of New Brunswick in Fredericton, New Brunswick, Canada. The code is utilized in paper *Stalls in Java Applications on the x86 Platforms*.

## Directory Structure

* /benchmarks - source code for base utilities and benchmarks
    * /baseUtils/Benchmark.java - an abstract class inherited by all benchmarks.
    * /baseUtils/Annotations - contains annotations to mark the benchmarks and for processing. Currently there are two annotations for OpenJ9, DontInLine indicates a method should not be inlined by the JIT compiler, and Trace indicates the trace log file of the method with certain optimization level should be generated by the JVM. For HotSpot JVM, optimization levels and APIs to control JVM behaviors are different.
    * /DataAccess - contains benchmarks for memory access patterns.
        * /DenseMatrix - contains dense matrix implementations and algebra benchmarks (i.e., WrapMatrix and PrimitiveMatrix).
        * /SparseMatrix - contains sparse matrix implementation and algebra benchmark using linked lists
        * /SearchTree - contains N-ary tree implementation using LCRS and the benchmark performing DFS on a shared tree.
    * /InterfaceChecking - contains the benchmark for type checking.
    * /ObjectLayout - contains the benchmark for huge object handling.

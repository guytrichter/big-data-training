# big-data-training
hadoop training

Build a "master dataset" associated with the lambda archuitecture.  

A typical lambda  architecutre consists of 3 layers:
1. Batch layer
2. Serving layer
3. Speed layer

Here, we will focus on building the batch layer.

In the lambda architecture, the batch layer is the "master dataset", and is considered to be the "source of truth".  It is an ever growing dataset (distibuted filesystem), that appends immutable timestamped "facts" to the dataset using vertical partitioning.  The "master dataset" thus does not maintain state (no updates - all facts are timestamped and immutable).

Batch functions running on top of the "master dataset" will serve the serving layer, which in turn will serve the client, thus the dataset must be reliable and error prone.  In addition, batch parallel queries need to run on top of the master dataset.

Therefore, having a simple yet structured dataset helps achieve reliability and serves the necessary performance needed.

The tools we are going to for implementing the "master dataset" are:
Hadoop fs 
Hadoop mr
apache thrift
pail


# PART 1
 
##Tools/Libraries
* Build tool is gradle.
* Test framework is junit.
* Command line parsing library is JCommander.
* No DI framework used since this is an assignment and little code. In production I would make use of a DI framework.
* I used Gson for serialisation of metadata.

##Assumptions
* Non existing directories should be created upon need without user intervention.
* Multiple create master data set requests to the same directory is a user error.
* An update can give a folder name that is not present in master data set. It will be processed as if there were such a folder with 0 size.
* I do not support any manual data addition from user. Such operation would require metadata update to be consistent.
* Non empty master data set directory is a user error when creating new master data set. (Because; in case of an update, files that are not part of master data set yet inside the directory can be appended and corrupted. This can still happen if the user puts folders in master data set after it is created. That is user error.)  
* File encoding is not configurable. I used UTF-8. My code also knows how to calculate UTF-8 file size only. Code is extensible though. 
* Length of each line is irrelevant.
* Filenames are irrelevant.
* I can put some lock and metadata in to the master data set directory. 
* Standard file system is used in implementation but simple refactor will enable the use of network file systems. Relevant abstractions are made in code. Design discussion avoids this assumption.


##Design Decisions
####Use of Independent SubTasks
**rationale:** The idea to single threadedly populate a folder is not scalable to the big data sizes in cases which the storage medium is not the bottleneck. This case commonly arises when the storage medium is over the network. Thus the operation to populate a folder should be divided into independent sub tasks. Subtask could be defined by the state each target file but without loss of generality this task can be defined only by the incremental operation on the file. This level of generality will allow simultaneous tasks to be executed on the same file.

A file append task is defined for this purpose. The file appender task does not assume the existence of the given file. It is generalized to the point that it only knows a file location, the amount of data to write, a file encoding, a file size calculation mechanism and a data supplier.

####Use of Shared Metadata
**rationale:** It is possible that for any given time there are ongoing data generation or data update tasks being executed for the given master data set. Thus the current size of the files can not be used to determine the state of a folder or master data set.

A minimal metadata is defined. Metadata is atomically written and read with a lock mechanism. Metadata is locked briefly while the new state of the master data set is calculated. Thus almost no performance hit.
  
####Precalculation of Datasize Appended by a Task
**rationale:** It is possible to query the file system to monitor file size. That would not work in this case because there may be multiple appenders to the same file simultaneously. Also that will be an extra hit on the filesystem that is possibly the bottleneck.

To overcome this limitation each file appender task keeps track of the amount of data it has written. Thus the file encoding must be known by the file appender.
  
####<a name="approach_discussion"></a>Reaching to a Given State Incrementally vs Applying Given Changes Toward Undetermined State  

##Limitations/Things to Be Improved

1. The operation to write to the file is not idempotent. Any failure that left task incomplete can not repair itself on retry. Since there may be data already written on file during the failed execution a task that that only knows how much more data to write to each file will overflow data on retry.
There is not retry mechanism implemented in the assignment but there are multiple ways to overcome this issue;
    * Each file can be locked before executing a file append task. So that multiple append tasks are not executed on same file. The task should also read and atomically persist the finishing size of the file before starting the task. This will make the task idempotent with cost of scalability. Loss of scalability is reasonable in most use cases. The size of each file is subject to configuration. Total data size can still grow to multiple files anyway.
    * There can be a check and repair task that executes when no other writing task is executed on a master data set. This task can receive the latest registered state of the master data set and queue new data update tasks. Actually all project could be composed of this kind of a task; see [this](#approach_discussion) for evaluation of this approach. 
  
2. It is not good to store metadata in hidden files. It should be persisted on proper DBs. Both SQL and NoSql dbs can be used. Also there is not much of a scalability concern. Metadata can be quite small.

3. Depending on the type of storage medium too high level of parallelism may be deficient for final throughput on storage. Optimisations can be applied by the storage medium.

4. The metadata can be enriched to keep complete history of updates together with the final status. 

5. I would keep a lot more data to monitor/stop/restart/recover status of each task at all times.

6. If requirements are extended for ability to return to a specific state. Simple byte offste checkpointing can be implemented. 

7. In memory producer/queue/consumer/storage mechanism is implemented. All queue and consumer mechanism can be externalized for scalability/monitoring/reliability etc.

###1.1 Data Generation

####1.1.1 Design Decisions
    * Master Dataset Creator is composed of validation, master dataset directory creation and delegation of file creation tasks to data update tool.
    * Random generation is done by a ThreadLocalRandom generator as it is more performant than both Random and SecureRandom generators.
###1.2 Data Update


####1.2.2 Design Decisions
    
    I made use of shared metadata in order to determine the last registered file sizes.
    * The final number of files and their sizes are precalculated in order to be able to create well defined tasks that can execute independently for each file.
    * The granularity of file append tasks can be further reduced depending in the performance needs.
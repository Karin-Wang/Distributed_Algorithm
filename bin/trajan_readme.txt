step 1: copy folder 'trajan' to machine A and machine B
step 2: in machine A, "start rmiregistry [portNumber]" at this directory
step 3: in machine A, run trajan.Server with args[] = {[portNumber], [ServerName]}
step 4: in machine B, run trajan.Client with args[] = {[numberToBeProcessed], [LAN IP of machine A], [portNumber], [ServerName]}

note: step 4 can also be done on machine A (local invocation), by setting args[1] to 127.0.0.1
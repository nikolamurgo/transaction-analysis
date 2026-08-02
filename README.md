# transaction analysis

Builds a linkability network from the Ethereum Transaction Network for addresses
that traded the Bored Ape Yacht Club NFT collection. Three versions: `sequential/`,
`parallel/`, `distributed/`.

## Data

The `data/` folder is not in the repo and must be added before running:

```
data/prog3ETNsample.csv        ETN transactions (4.9 GB)
data/boredapeyachtclub.csv     NFT transfers
data/blacklist/*.json          blacklisted addresses (6 files)
```

## Running

Run every command from the project root, the code uses relative `data/` paths.

Sequential:

```bash
javac -d sequential/out sequential/src/*.java sequential/src/utils/*.java && java -cp sequential/out Main
```

Parallel:

```bash
javac -d parallel/out parallel/src/*.java parallel/src/utils/*.java && java -cp parallel/out Main
```

Distributed (MPJ Express, `-np` = number of ranks):

```bash
javac -cp distributed/mpj-v0_44/lib/mpj.jar -d distributed/out distributed/src/*.java distributed/src/utils/*.java
```

```bash
export MPJ_HOME=$(pwd)/distributed/mpj-v0_44 && $MPJ_HOME/bin/mpjrun.sh -np 2 -cp distributed/out -Xmx4g Main
```

`-Xmx4g` is needed for `-np 2` or more, all ranks share one JVM heap.

## Output

`data/linkability.csv` (sender,receiver,weight) and the edge count per weight
printed to the console.

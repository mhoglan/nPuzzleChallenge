# N Puzzle Challenge

Visit the repository wiki to get the latest documentation and usage.

* https://github.com/mhoglan/nPuzzleChallenge/wiki

No need to build the repository, a Docker container is available.

## Quick Start

```
docker pull docker.io/mhoglan/npuzzlechallenge
docker run -d --name npuzzlechallenge -p 8080:8080 -p 8081:8081 mhoglan/npuzzlechallenge
```

Puzzle challenge server is now available for interaction via port 8080.  

Get started with the first puzzle by sending a POST with puzzleId: 100;  The location header returned will be the instance of the challenge to interact with.  

Example using `curl` to execute requests.

```
❯❯❯ curl -s --data "puzzleId=100" -D - -X POST http://localhost:8080/challenges -o /dev/null
HTTP/1.1 201 Created
Date: Thu, 17 Sep 2015 15:45:20 GMT
Location: http://localhost:8080/challenges/1
Content-Length: 0
❯❯❯ curl -s -X GET http://localhost:8080/challenges/1/view
Board
-----------------
|       4       |
|               |
|   3   A   1   |
|               |
|       2       |
-----------------

Unused Pieces: 0
❯❯❯ curl -s --data "x1=0&y1=0" -D - -X POST http://localhost:8080/challenges/1/action/rotate
HTTP/1.1 200 OK
Date: Thu, 17 Sep 2015 15:49:56 GMT
Content-Length: 0

❯❯❯ curl -s -X GET http://localhost:8080/challenges/1/view
Board
-----------------
|       3       |
|               |
|   2   A   4   |
|               |
|       1       |
-----------------

Unused Pieces: 0
```

JSON view is available at: `GET http://localhost:8080/challenges/1`

TEXT view is available at: `GET http://localhost:8080/challenges/1/view`

IMAGE view is available at: `GET http://localhost:8080/challenges/1/viewimage`

API usage and interaction detailed in the wiki.

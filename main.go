//go:generate protoc -I src/main/proto --go_out=plugins=grpc:. echo_service.proto
package main

import (
	"context"
	"flag"
	"fmt"
	echo "github.com/alimate/measurement/g/grpc"
	"google.golang.org/grpc"
	"log"
	"sync"
	"time"
)

func main() {
	var requests int
	flag.IntVar(&requests, "requests", 10_000, "The number of requests")
	flag.Parse()

	conn, err := grpc.Dial("localhost:9000", grpc.WithInsecure())
	if err != nil {
		log.Fatal("Failed to connect to the server", err)
	}

	wg := &sync.WaitGroup{}
	wg.Add(requests)
	msg := &echo.Message{
		Content: "Hello",
	}

	startedAt := time.Now()
	for i := 0; i < requests; i++ {
		go func() {
			_, err := echo.NewEchoServiceClient(conn).Echo(context.Background(), msg)
			if err != nil {
				fmt.Println("Failed to handle a request", err)
			}

			wg.Done()
		}()
	}

	wg.Wait()
	duration := time.Since(startedAt).Milliseconds()
	fmt.Println("Took: ", duration)
	fmt.Println("RPS: ", float64(requests)*1000.0/float64(duration))
}

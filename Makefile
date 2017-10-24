TOPIC=words
ZOOKEEPER=zookeeper:32181
KAFKA=kafka:29092

RUNNER=direct-runner

up:
	docker-compose up

upd:
	docker-compose up -d

fup:
	docker-compose up --force-recreate

down:
	docker-compose down

topic:
	docker-compose exec kafka kafka-topics --create --topic $(TOPIC) --partitions 1 --replication-factor 1 --if-not-exists --zookeeper $(ZOOKEEPER)

describe:
	docker-compose exec kafka kafka-topics --describe --topic $(TOPIC) --zookeeper $(ZOOKEEPER)

offset:
	docker-compose exec kafka kafka-run-class kafka.tools.GetOffsetShell --broker-list $(KAFKA) --topic $(TOPIC) --time -1

dump:
	docker-compose exec kafka kafka-console-consumer --bootstrap-server $(KAFKA) --topic $(TOPIC) --new-consumer --from-beginning --max-messages 5

producer:
	docker-compose run beam mvn compile exec:java -Dexec.mainClass=com.andrewjones.KafkaProducerExample -P$(RUNNER)

vproducer:
	docker-compose run beam mvn compile exec:java -Dexec.mainClass=com.andrewjones.KafkaProducerValuesExample -P$(RUNNER)

consumer:
	docker-compose run beam mvn compile exec:java -Dexec.mainClass=com.andrewjones.KafkaConsumerExample -P$(RUNNER)

clean: clean-docker clean-files

clean-docker:
	docker-compose rm -f

clean-files:
	rm wordcounts*

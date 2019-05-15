# Stations API

## Configuration

### Environment Variables

| Variable                | Description                                    |
|:------------------------|:-----------------------------------------------|
| `STATION_POSTGRES_HOST` | PostgreSQL hostname                            |
| `STATION_POSTGRES_PORT` | PostgreSQL Port (currently unused)             |
| `STATION_POSTGRES_DB`   | PostgreSQL database to use                     |
| `STATION_POSTGRES_USER` | PostgreSQL user for authentication             |
| `STATION_POSTGRES_PW`   | Corresponding password for the PostgreSQL user |
| `STATION_MONGODB_URI`   | MongoDB connection URI                         |
| `STATION_MONGODB_NAME`  | Name of the MongoDB database to use            |

## Usage

```
> make help
usage: make [target]

targets:
  help                 Show this help
  verify               Run Maven build phase `verify`
  build                Build the Docker image
  login                Login to the Docker registry
  push                 Push Docker image to the registry
  deploy-dev           Set image of to the newly created image
  dev                  Run verify, build, login, push and deploy-dev
  clean                Cleanup Maven build (mvn clean)
```

https://devcenter.heroku.com/articles/using-a-custom-maven-settings-xml

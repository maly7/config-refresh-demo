# Config Refresh Demo

This application demonstrates using `spring-cloud-starter` to refresh Configuration Properties for applications running against Azure Spring Apps Enterprise Tier.

## Getting Started

For the configuration repo - https://github.com/maly7/cloud-config-test

Create the application in Azure Spring Apps:

```shell
az spring app create -n config-refresh-demo --assign-endpoint true
```

Bind the application to Application Configuration Service:

```shell
az spring application-configuration-service bind --app config-refresh-demo
```

Define the necessary configuration in Application Configuration Service using the forked git repository:

```shell
az spring application-configuration-service git repo add \
  --name cloud-config-test \
  --label main \
  --uri https://github.com/MY-FORK/cloud-config-test.git \
  --patterns "application/default,config-refresh-demo/dev"
```

Deploy the application:

```shell
az spring app deploy \
  --name config-refresh-demo \
  --source-path . \
  --build-env BP_JVM_VERSION=17 \
  --env ""
  --config-file-patterns "application/default,config-refresh-demo/dev"
```

Fetch the application url:

```shell
APP_URL=$(az spring app show -n config-refresh-demo | jq -r '.properties.url')
```

Access the application endpoints `/menu/special` and `coffee/special`

```shell
curl $APP_URL/menu/special
curl $APP_URL/coffee/special
```

And you should see output like:
```console
❯ curl $APP_URL/menu/special
Seafood Gumbo%                                                                                                                                                                                                                                                                          
❯ curl $APP_URL/coffee/special
Colombia Bourbon%
``` 

## Demoing Configuration Refresh

Refresh the application config using the `/actuator/refresh` endpoint:

```shell
curl "${APP_URL}/actuator/refresh" -d {} -H "Content-Type: application/json" 
```

And the output should be empty because there haven't been any configuration changes:

```console
❯ curl "${APP_URL}/actuator/refresh" -d {} -H "Content-Type: application/json"
[]%   
```

Update the file `config-refresh-demo-dev.yml` in the configuration repository fork to include the following yaml:

```yaml
coffee:
  special: Colombia Aponte Village

menu:
  special: Seafood Lasagna
```

Wait a few minutes for the configuration to update in Application Configuration Service, then refresh using the actuator.

```console
❯ curl "${APP_URL}/actuator/refresh" -d {} -H "Content-Type: application/json"
["coffee.special","menu.special"]%      
```

This time, you should see the response `["coffee.special", "menu.special"]` that property changes were observed in these
properties.

> Note: Properties will be reported by this endpoint even if they are not considered under the refresh scope.

Now access the endpoint again:

```shell
curl $APP_URL/coffee/special
```

And you'll notice that in the output that the `/coffee/special` has been updated because the `Coffee` bean is annotated 
with `@RefreshScope` as well as the consuming Bean `MenuController`. 

## Appendix

Notes:
* If you hava a `DataSource` bean that is a `HikariDataSource`, it can not be refreshed. It is the default value for `spring.cloud.refresh.never-refreshable`. Choose a different `DataSource` implementation if you need it to be refreshed.
* `@RefreshScope` must be on the consuming `@Bean` and not just the one supplying the configuration
* `@RefreshScope` works (technically) on a @`Configuration` class, but it might lead to surprising behavior. For example, it does not mean that all the `@Beans` defined in that class are themselves in @RefreshScope. Specifically, anything that depends on those beans cannot rely on them being updated when a refresh is initiated, unless it is itself in `@RefreshScope`. In that case, it is rebuilt on a refresh and its dependencies are re-injected. At that point, they are re-initialized from the refreshed `@Configuration`).

Links:
* [Refresh Scope](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#refresh-scope)
* [Dynamic Configuration Properties in Spring Boot and Spring Cloud](https://gist.github.com/dsyer/a43fe5f74427b371519af68c5c4904c7)
* [Application Configuration Service](https://docs.vmware.com/en/Application-Configuration-Service-for-VMware-Tanzu/index.html)

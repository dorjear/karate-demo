function() {
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);

  if (!env) {
    env = 'default';
  }

  var config = {
    urlBasePath: 'https://jsonplaceholder.typicode.com'
  };

  if (env == 'dev') {
    config.urlBasePath = 'https://dev.example.com';
  } else if (env == 'qa') {
    config.urlBasePath = 'https://qa.example.com';
  } else if (env == 'prod') {
    config.urlBasePath = 'https://prod.example.com';
  }

  karate.log('Config object:', config);

  karate.configure('logPrettyRequest', true);
  karate.configure('logPrettyResponse', true);

  return config;
}

const path = require('path');

module.exports = {
  devServer: {
    https: {
      key: fs.readFileSync(path.resolve(__dirname, 'ssl/ssl_http_front.key')),
      cert: fs.readFileSync(path.resolve(__dirname, 'ssl/ssl_http_front.cer')),
    },
  },
};
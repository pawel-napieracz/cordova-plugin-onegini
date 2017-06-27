const path = require('path');
const webpack = require('webpack');

module.exports = {
  entry: {
    onegini: './src/js/onegini.js'
  },

  output: {
    path: path.resolve('./dist/'),
    filename: '[name].js',
    library: 'onegini',
    libraryTarget: 'umd'
  },

  resolve: {
    extensions: ['.js'],
    modules: [
      'node_modules'
    ]
  },

  devtool:  '#source-map',

  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel-loader'
      }
    ]
  }
};

if (process.env.NODE_ENV === 'production') {
  module.exports.devtool = '#source-map';

  module.exports.plugins = (module.exports.plugins || []).concat([
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: '"production"'
      }
    }),
    new webpack.optimize.UglifyJsPlugin({
      sourceMap: true,
      compress: {
        warnings: false
      }
    }),
    new webpack.LoaderOptionsPlugin({
      minimize: true
    })
  ])
}

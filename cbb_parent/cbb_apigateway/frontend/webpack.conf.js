const path = require('path')
const webpack = require('webpack')
const isProd = process.env.NODE_ENV === 'production'
const MiniCssExtractPlugin = require('mini-css-extract-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const WebpackParallelUglifyPlugin = require('webpack-parallel-uglify-plugin')
const CleanWebpackPlugin = require('clean-webpack-plugin')
const UglifyJsWebpackPlugin = require('uglifyjs-webpack-plugin')
const ProgressBarPlugin = require('progress-bar-webpack-plugin')
const HappyPack = require('happypack')
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin')
// const tsImportPluginFactory = require('ts-import-plugin')
// 构造出共享进程池，进程池中包含5个子进程
const happyThreadPool = HappyPack.ThreadPool({
  size: 5
})
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
// purifycss-webpack

module.exports = {
  mode: isProd ? 'production' : 'development',
  entry: {
    // 应用入口
    app: ['babel-polyfill', './src/index.tsx'] // 打包入口
  },
  // externals:{
  //   BMap: 'BMap'
  // },
  output: {
    // 输出目录
    filename: 'js/[name]_bundle.js', // 入口分块的文件名模板 name代表entry对应的名字; hash代表 整个app打包完成后根据内容加上hash。一旦整个文件内容变更，hash就会变化
    chunkFilename: 'js/[name]_bundle.js', // 打包好之后的输出路径
    path: isProd ? path.resolve(__dirname, './target/generated-resources/public/dist/') : path.resolve(__dirname, './public/dist/'), // 所有输出文件的目标路径，必须是绝对路径（resolve）
    publicPath: isProd ? './dist/' : '/dist/' // 输出解析文件目录 静态资源文件引用时的相对路径（加在引用静态资源前面的）
  },
  devServer: {
    contentBase: path.resolve(__dirname, './public'),
    compress: true, // 压缩
    historyApiFallback: true, // 任意的 404 响应都可能需要被替代为 index.html
    hot: true, // 热更新 依赖HotModuleReplacementPlugin
    // https: false,
    noInfo: true,
    open: true,
    progress: true,
    inline: true,
    overlay: {
      warnings: true,
      errors: true
    },
    host: '0.0.0.0',
    // host: '172.17.2.226',
    // port: 30010,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8090',
        secure: false,
        changeOrigin: true
      },
      '/login': {
        target: 'http://127.0.0.1:8090',
        secure: false,
        changeOrigin: true
      }
    }
  },
  optimization: {
    // 优化
    // minimizer: [new OptimizeCSSAssetsPlugin()],
    splitChunks: {
      chunks: 'all',
      minSize: 30000, // 模块大于30k会被抽离到公共模块
      minChunks: 1, // 模块出现1次就会被抽离到公共模块
      maxAsyncRequests: 5, // 异步模块，一次最多只能被加载5个
      maxInitialRequests: 3, // 入口模块最多只能加载3个
      name: true,
      cacheGroups: {
        default: {
          minChunks: 2,
          priority: -20,
          reuseExistingChunk: true,
          name: 'default'
        },
        vendors: {
          test: /[\\/]node_modules[\\/]/,
          priority: -10,
          name: 'vendors'
        }
      }
    },
    runtimeChunk: {
      name: 'runtime'
    }
  },
  devtool: isProd ? 'cheap-module-source-map' : 'cheap-module-eval-source-map', // 只能用在开发环境 牺牲了构建速度
  target: 'web', // bundle 应该运行的环境 枚举
  resolve: {
    // 解析模块请求的选项 不适用于对loader的解析
    // 用于查找模块的目录
    // modules: [
    //     "node_modules",
    //     path.resolve(__dirname, "app")
    // ],
    // 使用的扩展名
    extensions: ['.ts', '.tsx', '.js', '.jsx', '.json'],
    // 模块别名列表 相对于当前上下文导入
    alias: {
      '@components': path.resolve(__dirname, './src/components'),
      '@layouts': path.resolve(__dirname, './src/layouts'),
      '@pages': path.resolve(__dirname, './src/pages'),
      '@assets': path.resolve(__dirname, './src/assets'),
      '@utils': path.resolve(__dirname, './src/utils'),
      '@config': path.resolve(__dirname, './src/config'),
      '@stores': path.resolve(__dirname, './src/stores')
    }
  },
  module: {
    // 关于模块配置
    // 模块规则（配置loader 解析器等）
    rules: [
      // 匹配条件，每个选项都接收一个正则表达式或字符串 test 和 include 具有相同的作用，都是必须匹配选项 exclude 是必不匹配选项（优先于 test 和 include）
      // 最佳实践：只在 test 和 文件名匹配 中使用正则表达式 在 include 和 exclude 中使用绝对路径数组 尽量避免 exclude，更倾向于使用 include

      // 2. ts-loader的配置
      {
        test: /\.(jsx|tsx|js|ts)$/,
        loader: 'awesome-typescript-loader',
        exclude: /node_modules/
      },
      // 1.CSS SCSS loader配置  应用多个loader和选项
      {
        test: /\.(le|c)ss$/,
        exclude: [/[\\/]node_modules[\\/].*antd/],
        use: [
          'css-hot-loader',
          MiniCssExtractPlugin.loader, // 将 CSS 从主应用程序中分离
          {
            loader: 'css-loader',
            options: {
              importLoaders: 1, // 在 css-loader 前应用的 loader 的数量 0 => 无 loader(默认); 1 => postcss-loader; 2 => postcss-loader, sass-loader
              modules: false,
              localIdentName: '[name]_[local]_[hash:base64:6]', // 配置生成的标识符
              minimize: true // 压缩
            }
          },
          {
            loader: 'postcss-loader',
            options: {
              sourceMap: !isProd,
              ident: 'postcss'
            }
          },
          {
            loader: 'resolve-url-loader'
          },
          {
            loader: 'less-loader',
            options: {
              javascriptEnabled: true,
              sourceMap: !isProd
            }
          }
        ]
      },
      // 3. 加载图片
      {
        test: /\.(png|jpg|gif|svg|ico|cur)(\?[=a-z0-9]+)?$/,
        use: [{
          loader: 'url-loader',
          options: {
            limit: 1 * 1024,
            name: 'images/[hash:6].[ext]',
            fallback: 'file-loader'
          }
        }]
      },
      // 4. 加载字体
      {
        test: /\.(ttf|eot|otf|woff(2)?)(\?[\s\S]+)?$/,
        use: [{
          loader: 'file-loader',
          options: {
            name: 'fonts/[hash:6].[ext]'
          }
        }]
      },
      // 5. 加上babel结合happypack
      {
        enforce: 'pre',
        test: /\.js$/,
        exclude: /node_modules/,
        // exclude: path.resolve(__dirname, 'node_modules'),
        use: [
          'happypack/loader?id=babel'
        ]
      }
    ]
  },
  plugins: [
    new ProgressBarPlugin({
      format: 'build [:bar] :percent (:elapsed seconds)',
      clear: false,
      width: 60
    }),
    new webpack.optimize.ModuleConcatenationPlugin(),
    new MiniCssExtractPlugin({
      filename: 'style/[name].css'
      // chunkFilename: 'style/[id].css'
    })
  ].concat(!isProd ? [
    new webpack.HotModuleReplacementPlugin()
  ] : [
    new BundleAnalyzerPlugin({
      analyzerMode: 'static', // static disabled
      analyzerHost: '127.0.0.1',
      analyzerPort: '8888',
      reportFilename: 'report.html',
      defaultSizes: 'parsed', // stat gzip
      openAnalyzer: false,
      generateStatsFile: false,
      statsFilename: 'stats.json',
      statsOptions: null,
      excludeAssets: null,
      logLevel: 'info'
    }),
    new CleanWebpackPlugin([isProd ? './target/generated-resources/public':'./public']),
    new WebpackParallelUglifyPlugin({
      uglifyES: {
        mangle: false,
        output: {
          beautify: false,
          comments: false
        },
        compress: {
          warnings: false,
          drop_console: true,
          collapse_vars: true,
          reduce_vars: true
        }
      }
    }),
    new HtmlWebpackPlugin({
      title: 'WIMP',
      hash: true,
      filename: '../index.html',
      template: './public/template.html',
      favicon: path.resolve(__dirname, 'public/favicon.ico'),
      minify: {
        removeComments: true,
        collapseWhitespace: true,
        removeRedundantAttributes: true,
        useShortDoctype: true,
        removeEmptyAttributes: true,
        removeStyleLinkTypeAttributes: true,
        keepClosingSlash: true,
        minifyJS: true,
        minifyCSS: true,
        minifyURLs: true
      },
      chunksSortMode: 'none',
      cache: true
    }),
    new UglifyJsWebpackPlugin({
      sourceMap: true
    }),
    new HappyPack({
      // 用唯一的标识符 id 来代表当前的 HappyPack 是用来处理一类特定的文件
      id: 'babel',
      threadPool: happyThreadPool,
      // 如何处理 .js 文件，用法和 Loader 配置中一样 ?cacheDirectory
      loaders: ['babel-loader']
      // ... 其它配置项
    })
  ])
}

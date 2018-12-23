var gulp = require('gulp');

// Bower 
var bower = require('gulp-bower');

var sourcemaps = require('gulp-sourcemaps');
var minify = require('gulp-minify');
var cleanCSS = require('gulp-clean-css');
var htmlmin = require('gulp-htmlmin');
var strip = require('gulp-strip-comments');
var templateCache = require('gulp-angular-templatecache');
var changed = require('gulp-changed');
// var addsrc = require('gulp-add-src');

// Typescript
var ts = require('gulp-typescript');
var tsProject = ts.createProject('tsconfig.json', {
    typescript: require('typescript')
});

var exec = require('child_process').exec;

var clean = require('gulp-clean');
var del = require('del');
var filesExist = require('files-exist');

var ngAnnotate = require('gulp-ng-annotate');

// Configuraciones
var ngAnnotateOptions = {
    remove: false,
    add: true,
    single_quotes: true
};
var filesExistConfig = {
    checkGlobs: true,
    throwOnMissing: false
};
var paths = {
    views: ['src/**/*.html',
        'src/**/*.css',
        'src/**/*.svg',
        'src/**/*.png',
        'src/**/*.jpg',
        'src/**/*.gif'
    ],
    images: [
        'src/**/*.svg',
        'src/**/*.png',
        'src/**/*.jpg',
        'src/**/*.gif'
    ]
};
var htmlminOptions = {
    collapseWhitespace: true
};

// gulp.task('bower-release', function() {
//     return bower({
//         cmd: 'update',
//         directory: 'www/vendor'
//     });
// });

// gulp.task('bower-debug', function() {
//     return bower({
//         cmd: 'update'
//     });
// });

// gulp.task('minify-css', function() {
//     return gulp.src('src/**/*.css')
//         .pipe(changed('www'))
//         .pipe(cleanCSS({
//             compatibility: 'ie8'
//         }))
//         .pipe(gulp.dest('www'));
// });

// gulp.task('bower', ['bower-debug']);

// gulp.task('default', ['debug-fast']);
gulp.task('default', ['debug']);

// gulp.task('release', ['scripts-release', 'scripts-js', 'views-release', 'bower-installer-release'], function() {
//     return gulp.src([
//             'www/**/*model.js',
//             'www/**/*viewData.js',
//             'www/**/*.js.map'
//         ])
//         .pipe(clean());
// });


// gulp.task('views-release', ['templates-release', 'minify-index-html', 'minify-css'], function() {
//     return gulp.src(paths.images)
//         .pipe(changed('www'))
//         .pipe(gulp.dest('www'));
// });

// gulp.task('minify-html', function() {
//     return gulp.src('src/**/*.html')
//         .pipe(changed('www'))
//         .pipe(strip())
//         .pipe(htmlmin(htmlminOptions))
//         .pipe(gulp.dest('www'));
// });

// gulp.task('minify-index-html', function() {
//     return gulp.src('src/**/index.html')
//         .pipe(changed('www'))
//         .pipe(strip())
//         .pipe(htmlmin(htmlminOptions))
//         .pipe(gulp.dest('www'));
// });

// gulp.task('scripts-release', ['annotate-scripts'], function() {
//     return gulp.src([
//             '!www/vendor/**/*.js',
//             'www/**/*.js'
//         ])
//         .pipe(minify({
//             ext: {
//                 src: '-debug.js',
//                 min: '.js'
//             },
//             noSource: true,
//             ignoreFiles: ['-min.js']
//         }))
//         .pipe(gulp.dest('www'));
// });

// gulp.task('watch', function() {
//     gulp.watch('src/**/*.ts', ['compile-scripts']);
// });

// gulp.task('templates-release', function() {
//     return gulp.src(['src/**/*.html', '!app/index.html'])
//         .pipe(changed('www'))
//         .pipe(strip())
//         .pipe(htmlmin(htmlminOptions))
//         .pipe(templateCache({
//             filename: 'app/app.templates.js',
//             module: 'App',
//             moduleSystem: 'IIFE'
//         }))
//         .pipe(gulp.dest('www'));
// });

gulp.task('clean', function() {
    return gulp.src('www/*')
        .pipe(clean());
});


// gulp.task('bower-installer-release', ['bower-installer'], function() {
//     return gulp.src(['www/vendor/**/*.js'])
//         .pipe(minify({
//             ext: {
//                 src: '-debug.js',
//                 min: '.js'
//             },
//             noSource: true,
//             ignoreFiles: ['-min.js']
//         }))
//         .pipe(gulp.dest('www/vendor'));
// });

gulp.task('debug', ['scripts-debug', 'scripts-js', 'views-debug', 'bower-installer'], function() {
    return gulp.dest('www');
});

gulp.task('scripts-debug', ['annotate-scripts']);

gulp.task('annotate-scripts', ['compile-scripts'], function() {
    return gulp.src([
            '!www/vendor/**/*.js',
            'www/**/*module.js'
        ])
        .pipe(ngAnnotate(ngAnnotateOptions))
        .pipe(gulp.dest('www'));
});

gulp.task('compile-scripts', function() {
    return gulp.src(['src/**/*.ts'])
        .pipe(sourcemaps.init())
        .pipe(tsProject())
        .pipe(gulp.dest('www'));
});

gulp.task('scripts-js', function() {
    return gulp.src('src/**/*.js')
        .pipe(gulp.dest('www'));
});

gulp.task('views-debug', function() {
    return gulp.src(paths.views)
        .pipe(changed('www'))
        .pipe(gulp.dest('www'));
});

gulp.task('bower-installer', function(done) {
    exec('bower-installer', function(err, stdout, stderr) {
        console.error(stderr);
        del(filesExist([], filesExistConfig));
        done(err);
    });
});
require.config({
    waitSeconds: 15,
    baseUrl: '/assets/js/',
    paths: {
        'jquery': '../components/jquery/dist/jquery',
        'jquery-extends': '../components/jquery-extends/dist/jquery-extends',
        'react': '../components/react/react',
        'react-dom': '../components/react/react-dom',
        'react-bootstrap': '../components/react-bootstrap/react-bootstrap',
        'routes': '/routes'
    },
    shim: {
        'routes': {exports: 'Routes'}
    }
});

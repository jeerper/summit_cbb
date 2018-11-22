declare var require: {
    <T>(path: string): T;
    (paths: string[], callback: (...modules: any[]) => void): void;
    ensure: (paths: string[], callback: (require: <T>(path: string) => T) => void) => void;
}

declare module '*.gif'
declare module '*.jpg'
declare module '*.jpeg'
declare module '*.png'
declare module '*.svg'

declare module 'qs'
declare module 'classnames'
declare module 'lodash'
// declare module 'lodash-decorators'
declare module 'rc-animate'
declare module 'memoize-one'
declare module 'enquire-js'
declare module 'react-document-title'

declare module 'BMap'

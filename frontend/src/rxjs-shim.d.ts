declare module 'rxjs' {
  export class Observable<T> {
    constructor(subscribe?: any);
    pipe<R>(...operations: Array<(source: Observable<any>) => Observable<any>>): Observable<R>;
    subscribe(observer?: any, error?: any, complete?: any): any;
  }

  export function of<T>(...values: T[]): Observable<T>;

  export function catchError<T>(
    selector: (err: any, caught: Observable<T>) => Observable<T>
  ): (source: Observable<T>) => Observable<T>;

  export function switchMap<T, R>(
    project: (value: T) => Observable<R>
  ): (source: Observable<T>) => Observable<R>;
}

declare module 'rxjs/operators' {
  import { Observable } from 'rxjs';

  export function catchError<T>(
    selector: (err: any, caught: Observable<T>) => Observable<T>
  ): (source: Observable<T>) => Observable<T>;

  export function switchMap<T, R>(
    project: (value: T) => Observable<R>
  ): (source: Observable<T>) => Observable<R>;
}

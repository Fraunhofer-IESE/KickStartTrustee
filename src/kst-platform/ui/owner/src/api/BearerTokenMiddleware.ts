import { FetchParams, Middleware, RequestContext } from "owner-api/runtime";

const AUTHORIZATION = "Authorization";

class BearerTokenMiddleware implements Middleware {
  private static instance: BearerTokenMiddleware;

  private _token?: string;

  private constructor() {}

  static getInstance(): BearerTokenMiddleware {
    if (!BearerTokenMiddleware.instance) {
        BearerTokenMiddleware.instance = new BearerTokenMiddleware();
    }
    return BearerTokenMiddleware.instance;
  }

  public get token(): string | undefined {
    return this._token;
  }

  public set token(token: string | undefined) {
    this._token = token;
  }

  async pre(context: RequestContext): Promise<FetchParams | void> {
    const { url, init } = context;
    if (!this._token) {
      return { url, init };
    }
    const headers = new Headers(init.headers);
    headers.set(AUTHORIZATION, `Bearer ${this._token}`);
    init.headers = headers;
    return { url, init };
  }
}

export default BearerTokenMiddleware;

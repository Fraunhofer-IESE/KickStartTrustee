/// <reference types="vite/client" />
interface ImportMetaEnv {
  /**
   * Base url of the web ui
   */
  VITE_BASE_URL: string;
  /**
   * Rest url
   */
  VITE_REST_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}

interface Window {
  ENV: ImportMetaEnv;
}

declare const window: Window;

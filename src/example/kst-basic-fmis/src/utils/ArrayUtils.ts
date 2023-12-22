export function isSetEqual<T>(a: Set<T>, b: Set<T>): boolean {
  if (a.size === 0 && b.size === 0) {
    return true;
  }
  return a.size === b.size && Array.from(a).every((v) => b.has(v));
}

export function isArrayEqual<T>(a: Array<T>, b: Array<T>): boolean {
  if (a.length === 0 && b.length === 0) {
    return true;
  }
  return a.length === b.length && a.every((v, i) => v === b[i]);
};

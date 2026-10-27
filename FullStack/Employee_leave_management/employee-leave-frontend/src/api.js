// tiny fetch helper - returns parsed JSON, or plain text (delete returns text)
export async function api(path, method = 'GET', body) {
  const res = await fetch(path, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined,
  })
  const text = await res.text()
  try { return JSON.parse(text) } catch { return text }
}

// tiny fetch helper - returns parsed JSON, or plain text (delete returns text)
export async function api(path, method = 'GET', body) {
  const res = await fetch(path, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined,
  })
  const text = await res.text()
  let data = text
  try { data = JSON.parse(text) } catch { /* plain text */ }
  if (!res.ok) throw new Error(data?.message || data?.error || text || `Request failed (${res.status})`)
  return data
}

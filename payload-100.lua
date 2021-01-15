wrk.method = "POST"
wrk.headers["Content-Type"] = "application/json"

file = io.open("payload-100.json", "rb")
wrk.body = file:read("*a")

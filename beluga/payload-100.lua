wrk.method = "POST"
wrk.headers["Content-Type"] = "application/protobuf"

file = io.open("beluga/payload-100.proto.message", "rb")
wrk.body = file:read("*a")

wrk.method = "POST"
wrk.headers["Content-Type"] = "application/protobuf"

file = io.open("beluga/payload-10.proto.message", "rb")
wrk.body = file:read("*a")

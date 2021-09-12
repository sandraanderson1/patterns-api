# patterns-api


TODO

Guardian/OneService class:
- [x] exchangeToMono - if you care about the status code - return type will be ? extends throwable
- [ ] look at DownstreamExecutor 
- [ ] onErrorResume have default exception that always pops up

strategy pattern:
- [x] interface ie. callDownstream -> strategy that delegates to Guardian or One
- [x] have 2 different strategies - guardian + something else (another api) + header // DONE

chain responsibility pattern:
- decorator for each api calls made
- [x] 2 error handlers handing error responses from downstream ie. some error code will be handled differently between downstreams
- [x] eg. 1 handler handles only 5xx, the other both
- [x] look at SynacorStartTrackingHttpClientErrorHandler (calling to AbstractClass) -> not decorator pattern but similar idea


composition based stuff
//    https://github.com/matarrese/content-api-the-guardian - maybe use this?
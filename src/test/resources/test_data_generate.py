from datetime import datetime
#./hbase shell
tablename ='data2'
cfname='data'
print "create '{0}','{1}'".format(tablename,cfname)

putUrl = "put 'data2','websiteId-{0}','data:url','url{0}'"
putWebsiteId = "put 'data2','websiteId-{0}','data:title','title{0}'"
putUserId = "put 'data2','websiteId-{0}','data:content','content-{0}'"
putCount = "put 'data2','websiteId-{0}','data:count','{0}'"
putDate = "put 'data2','websiteId-{0}','data:date','{1}'"
for i in range(100,300):
    print  putUrl.format(i)
    print  putWebsiteId.format(i)
    print  putUserId.format(i)
    print  putCount.format(i)
    print putDate.format(i,datetime.now())
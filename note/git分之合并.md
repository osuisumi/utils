####  创建分支
```
#创建并切换到dev分支
git checkout -b dev

#或者
git branch dev
git checkout dev
```

####  开发完之后合并
> 想要合并到哪个版本  先切换到哪个版本,用merge命令合并

```
git checkout master
git merge dev
```

####  删除临时分支
git branch -d dev

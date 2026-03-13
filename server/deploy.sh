#!/bin/sh

# --quiet をつけないと、本当にデプロイするかどうかの確認を求められて
#.  そこでスクリプトが止まってしまうの --quiet をつける
gcloud app deploy --quiet --project animal-crossing-voice

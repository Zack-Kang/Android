# -*- coding: utf-8 -*-
# Generated by Django 1.9.3 on 2016-03-11 06:41
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0110_withdrawweekday'),
    ]

    operations = [
        migrations.RenameModel(
            old_name='WithdrawWeekday',
            new_name='Config',
        ),
        migrations.RenameField(
            model_name='config',
            old_name='weekday',
            new_name='withdraw_weekday',
        ),
    ]

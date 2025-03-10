# coding=utf-8
#-----------------------------------------------------
# Author: Yaqiang Wang
# Date: 2019-9-7
# Purpose: MeteoInfoLab glfigure module
# Note: Jython
#-----------------------------------------------------

from org.meteoinfo.chart.jogl import GLChartPanel

__all__ = ['GLFigure']

class GLFigure(GLChartPanel):

    def __init__(self, ax=None, **kwargs):
        '''
        Constructor
        
        :param ax: (*Axes3DGL*) 3D axes with JOGL
        '''
        self.axes = ax
        double_buffered = kwargs.pop('double_buffered', True)
        sample_buffers = kwargs.pop('sample_buffers', True)
        nsamples = kwargs.pop('nsamples', 4)
        cap = GLChartPanel.createCapabilities(double_buffered, sample_buffers, nsamples)

        super(GLFigure, self).__init__(cap)
        
    def set_axes(self, ax):
        '''
        Set axes.
        
        :param ax: (*Axes*) The axes.
        '''
        ax.figure = self
        self.axes = ax
        self.setPlot(ax.axes)

    def set_antialias(self, b=None, symbol=None):
        """
        Set figure antialias or not.

        :param b: (*boolean*) Antialias or not.
        :param symbol: (*boolean*) Set symbol antialias or not.
        """
        if b is None:
            b = not self.axes.get_antialias()
        self.axes.set_antialias(b)